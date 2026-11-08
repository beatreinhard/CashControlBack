package ch.reinhard.cashcontrol.modules.finanzen.application.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AusgabeVorschlagBo {
    private LocalDate datum;
    private String text;
    private AusgabeKategorieBo kategorie;
    private String empfaenger;
    private BigDecimal betrag;

    private String ausgabeId; // This field will hold the ID of the created AusgabeBo after approval
    private AusgabeVorschlagStatusBo status;  // This field will hold the status of the Vorschlag (e.g., OFFEN, GENEHMIGT, ABGELEHNT)
    private String contentHash; // This field will hold the hash of the content for integrity check
    private String dateiname; // Name der importierten Datei, für die Nachvollziehbarkeit
    private LocalDate importiertAm; // This field will hold the date when the Vorschlag was imported
    private LocalDate genehmigtAm; // This field will hold the date when the Vorschlag was approved


    public void genehmigen() {
        // TODO Implement the logic to approve the Vorschlag
    }

    public void ablehnen() {
        // TODO Implement the logic to reject the Vorschlag
    }

    public boolean isVollstaendig() {
        return datum != null && text != null && kategorie != null && empfaenger != null && betrag != null;
    }

    public AusgabeBo toAusgabeBo() {
        return new AusgabeBo()
                .setDatum(this.datum)
                .setText(this.text)
                .setKategorie(this.kategorie)
                .setEmpfaenger(this.empfaenger)
                .setBetrag(this.betrag);
    }
}
