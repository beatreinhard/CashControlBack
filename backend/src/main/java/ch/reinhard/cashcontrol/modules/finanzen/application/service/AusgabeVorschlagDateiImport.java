package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport.ImportDokument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Speichert ein einzelnes Import-Dokument in einer eigenen Transaktion.
 * <p>
 * Eigene Bean, weil {@code @Transactional} über einen Proxy wirkt: Ein Aufruf aus derselben
 * Klasse ({@code this.importiere(...)}) würde am Proxy vorbeigehen und ohne Transaktion laufen.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class AusgabeVorschlagDateiImport {

    // TODO AusgabeVorschlagPersistencePort injizieren, sobald Tabelle und Repository existieren

    /**
     * @return {@code true}, wenn der Vorschlag neu gespeichert wurde, {@code false} bei einem Duplikat
     */
    @Transactional
    public boolean importiere(ImportDokument dokument) {
        if (existiertContentHash(dokument.hash())) {
            log.info("Datei {} wurde bereits importiert (Hash {})", dokument.dateiname(), dokument.hash());
            return false;
        }
        // TODO persistencePort.createAusgabeVorschlag(dokument.vorschlag());
        //      Unique-Constraint auf content_hash verhindert Duplikate bei parallelen Läufen
        var vorschlag = dokument.vorschlag();
        log.debug("Vorschlag aus {}: {} {} {}", dokument.dateiname(),
                vorschlag.getDatum(), vorschlag.getEmpfaenger(), vorschlag.getBetrag());
        return true;
    }

    private boolean existiertContentHash(String hash) {
        // TODO return persistencePort.existiertContentHash(hash);
        return false;
    }
}
