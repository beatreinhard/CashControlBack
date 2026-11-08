package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.fileimport;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeKategorieBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagStatusBo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AusgabeVorschlagJsonImportAdapterTest {

    private static final String GUELTIG = """
            {
              "datum": "2026-10-26",
              "empfaenger": "SERAFE AG",
              "kategorie": "KOMMUNIKATION_MEDIEN",
              "text": "Radio- und Fernsehabgabe",
              "betrag": 321.60
            }
            """;

    @TempDir
    Path verzeichnis;

    private AusgabeVorschlagJsonImportAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AusgabeVorschlagJsonImportAdapter(verzeichnis, JsonMapper.builder().build());
    }

    @Test
    void gueltigeDatei_wirdAlsDokumentGeliefertUndBleibtLiegen() throws Exception {
        Files.writeString(verzeichnis.resolve("serafe.json"), GUELTIG);

        var dokumente = adapter.findeNeueDokumente();

        assertThat(dokumente).hasSize(1);
        var dokument = dokumente.getFirst();
        assertThat(dokument.dateiname()).isEqualTo("serafe.json");
        assertThat(dokument.hash()).hasSize(64);
        var vorschlag = dokument.vorschlag();
        assertThat(vorschlag.getDatum()).isEqualTo(LocalDate.of(2026, 10, 26));
        assertThat(vorschlag.getEmpfaenger()).isEqualTo("SERAFE AG");
        assertThat(vorschlag.getKategorie()).isEqualTo(AusgabeKategorieBo.KOMMUNIKATION_MEDIEN);
        assertThat(vorschlag.getBetrag()).isEqualByComparingTo(new BigDecimal("321.60"));
        assertThat(vorschlag.getStatus()).isEqualTo(AusgabeVorschlagStatusBo.OFFEN);
        assertThat(vorschlag.getContentHash()).isEqualTo(dokument.hash());
        assertThat(vorschlag.getDateiname()).isEqualTo("serafe.json");
        // Verschoben wird erst durch markiereVerarbeitet, also nach dem Commit
        assertThat(verzeichnis.resolve("serafe.json")).exists();
    }

    @Test
    void markiereVerarbeitet_verschiebtNachVerarbeitet() throws Exception {
        Files.writeString(verzeichnis.resolve("serafe.json"), GUELTIG);
        var dokument = adapter.findeNeueDokumente().getFirst();

        adapter.markiereVerarbeitet(dokument);

        assertThat(verzeichnis.resolve("serafe.json")).doesNotExist();
        assertThat(verzeichnis.resolve("verarbeitet/serafe.json")).exists();
    }

    @Test
    void gleicherInhaltUnterAnderemNamen_ergibtGleichenHash() throws Exception {
        Files.writeString(verzeichnis.resolve("a.json"), GUELTIG);
        Files.writeString(verzeichnis.resolve("b.json"), GUELTIG);

        var dokumente = adapter.findeNeueDokumente();

        assertThat(dokumente).hasSize(2);
        assertThat(dokumente.get(0).hash()).isEqualTo(dokumente.get(1).hash());
    }

    @Test
    void ungueltigesJson_wirdNachFehlerVerschoben() throws Exception {
        Files.writeString(verzeichnis.resolve("kaputt.json"), "{ \"datum\": ");

        assertThat(adapter.findeNeueDokumente()).isEmpty();
        assertThat(verzeichnis.resolve("fehler/kaputt.json")).exists();
    }

    @Test
    void unbekannteKategorie_wirdNachFehlerVerschoben() throws Exception {
        Files.writeString(verzeichnis.resolve("kategorie.json"), GUELTIG.replace("KOMMUNIKATION_MEDIEN", "GIBTS_NICHT"));

        assertThat(adapter.findeNeueDokumente()).isEmpty();
        assertThat(verzeichnis.resolve("fehler/kategorie.json")).exists();
    }

    @Test
    void array_wirdNachFehlerVerschoben() throws Exception {
        Files.writeString(verzeichnis.resolve("liste.json"), "[" + GUELTIG + "]");

        assertThat(adapter.findeNeueDokumente()).isEmpty();
        assertThat(verzeichnis.resolve("fehler/liste.json")).exists();
    }

    @Test
    void andereDateien_werdenIgnoriert() throws Exception {
        Files.writeString(verzeichnis.resolve("rechnung.pdf"), "pdf");

        assertThat(adapter.findeNeueDokumente()).isEmpty();
        assertThat(verzeichnis.resolve("rechnung.pdf")).exists();
    }

    @Test
    void gleicherNameImArchiv_wirdNichtUeberschrieben() throws Exception {
        Files.createDirectories(verzeichnis.resolve("verarbeitet"));
        Files.writeString(verzeichnis.resolve("verarbeitet/serafe.json"), "alt");
        Files.writeString(verzeichnis.resolve("serafe.json"), GUELTIG);

        adapter.markiereVerarbeitet(adapter.findeNeueDokumente().getFirst());

        assertThat(Files.readString(verzeichnis.resolve("verarbeitet/serafe.json"))).isEqualTo("alt");
        try (var dateien = Files.list(verzeichnis.resolve("verarbeitet"))) {
            assertThat(dateien.count()).isEqualTo(2);
        }
    }
}
