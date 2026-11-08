package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport;

import java.util.List;

/**
 * Quelle für zu importierende Ausgabe-Vorschläge. Die Signatur kennt kein Dateisystem,
 * damit später auch andere Quellen (z.B. ein Upload) möglich sind.
 */
public interface AusgabeVorschlagQuellePort {

    /**
     * Liefert alle neuen, gültigen Dokumente. Ungültige Dokumente legt die Quelle selbst ab
     * (z.B. nach {@code fehler/}) und liefert sie nicht zurück.
     */
    List<ImportDokument> findeNeueDokumente();

    /**
     * Markiert ein Dokument als verarbeitet (z.B. Verschieben nach {@code verarbeitet/}).
     * Wird erst nach dem Commit aufgerufen.
     */
    void markiereVerarbeitet(ImportDokument dokument);
}
