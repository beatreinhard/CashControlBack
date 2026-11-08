package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagBo;

/**
 * Ein gelesenes Dokument aus der Quelle.
 *
 * @param dateiname Name des Dokuments in der Quelle
 * @param hash      SHA-256 des Inhalts, Schlüssel für die Duplikaterkennung
 * @param vorschlag der daraus erzeugte Vorschlag (Status OFFEN)
 */
public record ImportDokument(String dateiname, String hash, AusgabeVorschlagBo vorschlag) {
}
