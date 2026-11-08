package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.fileimport;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagStatusBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport.AusgabeVorschlagQuellePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport.ImportDokument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Liest Ausgabe-Vorschläge aus JSON-Dateien im Import-Verzeichnis.
 * <ul>
 *   <li>Eine Datei enthält genau ein JSON-Objekt, also eine Ausgabe.</li>
 *   <li>Ungültiges JSON wird mit Log-Eintrag nach {@code fehler/} verschoben und nicht zurückgegeben.</li>
 *   <li>{@link #markiereVerarbeitet} verschiebt die Datei nach {@code verarbeitet/}.</li>
 * </ul>
 * Dateien werden nie gelöscht, nur verschoben.
 */
@Slf4j
@Component
public class AusgabeVorschlagJsonImportAdapter implements AusgabeVorschlagQuellePort {

    static final String ORDNER_VERARBEITET = "verarbeitet";
    static final String ORDNER_FEHLER = "fehler";

    private static final DateTimeFormatter ZEITSTEMPEL = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private final Path verzeichnis;
    private final JsonMapper jsonMapper;

    public AusgabeVorschlagJsonImportAdapter(
            @Value("${cashcontrol.import.verzeichnis:/import}") Path verzeichnis,
            JsonMapper jsonMapper) {
        this.verzeichnis = verzeichnis;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public List<ImportDokument> findeNeueDokumente() {
        List<Path> dateien;
        try {
            dateien = findeJsonDateien();
        } catch (IOException e) {
            log.error("Import-Verzeichnis {} kann nicht gelesen werden", verzeichnis, e);
            return List.of();
        }
        log.info("Import Ausgabe-Vorschläge: {} Datei(en) in {}", dateien.size(), verzeichnis);

        List<ImportDokument> dokumente = new ArrayList<>();
        for (Path datei : dateien) {
            lese(datei).ifPresent(dokumente::add);
        }
        return dokumente;
    }

    @Override
    public void markiereVerarbeitet(ImportDokument dokument) {
        verschiebe(verzeichnis.resolve(dokument.dateiname()), ORDNER_VERARBEITET);
    }

    private List<Path> findeJsonDateien() throws IOException {
        if (!Files.isDirectory(verzeichnis)) {
            throw new IOException("Kein Verzeichnis: " + verzeichnis);
        }
        try (Stream<Path> stream = Files.list(verzeichnis)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".json"))
                    .sorted()
                    .toList();
        }
    }

    private Optional<ImportDokument> lese(Path datei) {
        var dateiname = datei.getFileName().toString();

        // Bytes nur einmal lesen: Hash und geparster Inhalt passen so garantiert zusammen
        byte[] inhalt;
        try {
            inhalt = Files.readAllBytes(datei);
        } catch (IOException e) {
            // Datei bleibt liegen und wird beim nächsten Lauf erneut versucht
            log.error("Datei {} kann nicht gelesen werden", dateiname, e);
            return Optional.empty();
        }
        var hash = sha256(inhalt);

        try {
            return Optional.of(new ImportDokument(dateiname, hash, parse(inhalt, dateiname, hash)));
        } catch (JacksonException e) {
            log.error("Datei {} enthält ungültiges JSON und wird nach {}/ verschoben: {}",
                    dateiname, ORDNER_FEHLER, e.getOriginalMessage());
            verschiebe(datei, ORDNER_FEHLER);
            return Optional.empty();
        }
    }

    private AusgabeVorschlagBo parse(byte[] inhalt, String dateiname, String hash) {
        JsonNode root = jsonMapper.readTree(inhalt);
        if (!root.isObject()) {
            throw new IllegalJsonException("Erwartet wird ein JSON-Objekt");
        }
        var dto = jsonMapper.treeToValue(root, AusgabeVorschlagJsonDto.class);
        return new AusgabeVorschlagBo()
                .setDatum(dto.datum())
                .setEmpfaenger(dto.empfaenger())
                .setKategorie(dto.kategorie())
                .setText(dto.text())
                .setBetrag(dto.betrag())
                .setStatus(AusgabeVorschlagStatusBo.OFFEN)
                .setContentHash(hash)
                .setDateiname(dateiname)
                .setImportiertAm(LocalDate.now());
    }

    private void verschiebe(Path datei, String ordner) {
        try {
            var zielOrdner = Files.createDirectories(verzeichnis.resolve(ordner));
            Files.move(datei, eindeutigesZiel(zielOrdner, datei.getFileName().toString()));
        } catch (IOException e) {
            // Beim nächsten Lauf verhindert der Hash ein Duplikat
            log.warn("Datei {} konnte nicht nach {}/ verschoben werden", datei.getFileName(), ordner, e);
        }
    }

    /**
     * Gibt es im Ziel schon eine Datei mit gleichem Namen, wird ein Zeitstempel angehängt,
     * damit keine archivierte Datei überschrieben wird.
     */
    private static Path eindeutigesZiel(Path zielOrdner, String dateiname) {
        var ziel = zielOrdner.resolve(dateiname);
        if (!Files.exists(ziel)) {
            return ziel;
        }
        int punkt = dateiname.lastIndexOf('.');
        var basis = punkt > 0 ? dateiname.substring(0, punkt) : dateiname;
        var endung = punkt > 0 ? dateiname.substring(punkt) : "";
        return zielOrdner.resolve(basis + "_" + LocalDateTime.now().format(ZEITSTEMPEL) + endung);
    }

    private static String sha256(byte[] inhalt) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(inhalt));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 nicht verfügbar", e);
        }
    }

    /** Gültiges JSON, aber nicht im erwarteten Aufbau. Wird wie ungültiges JSON behandelt. */
    private static final class IllegalJsonException extends JacksonException {
        IllegalJsonException(String message) {
            super(message);
        }
    }
}
