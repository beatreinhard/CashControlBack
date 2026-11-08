package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeVorschlagServicePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport.AusgabeVorschlagQuellePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Service
public class AusgabeVorschlagService implements AusgabeVorschlagServicePort {

    private final AusgabeVorschlagQuellePort quellePort;
    private final AusgabeVorschlagDateiImport dateiImport; // andere Bean, damit @Transactional über den Proxy greift
    private final AtomicBoolean importLaeuft = new AtomicBoolean(false);

    @Override
    public List<AusgabeVorschlagBo> getAllAusgabeVorschlag() {
        // TODO Implement the logic to retrieve all AusgabeVorschlagBo
        return List.of();
    }

    /**
     * Bewusst ohne {@code @Transactional}: Jede Datei läuft in einer eigenen Transaktion
     * ({@link AusgabeVorschlagDateiImport#importiere}) und wird erst nach deren Commit verschoben.
     *
     * @throws ImportLaeuftBereitsException wenn bereits ein Import läuft
     */
    @Override
    public void importAusgabeVorschlag() {
        if (!importLaeuft.compareAndSet(false, true)) {
            throw new ImportLaeuftBereitsException();
        }
        try {
            int importiert = 0;
            int uebersprungen = 0;
            int fehlerhaft = 0;
            for (var dokument : quellePort.findeNeueDokumente()) {
                try {
                    boolean neu = dateiImport.importiere(dokument); // Commit ist hier bereits erfolgt
                    quellePort.markiereVerarbeitet(dokument);
                    if (neu) {
                        importiert++;
                    } else {
                        uebersprungen++;
                    }
                } catch (RuntimeException e) {
                    // Rollback ist erfolgt, die Datei bleibt liegen und wird beim nächsten Lauf erneut versucht
                    log.error("Datei {} konnte nicht gespeichert werden", dokument.dateiname(), e);
                    fehlerhaft++;
                }
            }
            log.info("Import Ausgabe-Vorschläge abgeschlossen: importiert={}, übersprungen={}, fehlerhaft={}",
                    importiert, uebersprungen, fehlerhaft);
        } finally {
            importLaeuft.set(false);
        }
    }
}
