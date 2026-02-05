package ch.reinhard.cashcontrol.core.domainevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

@Getter
public class AusgabeUpdatedEvent extends ApplicationEvent {
    private final String ausgabeId;
    private final LocalDate datum;
    private final String empfaenger;
    private final AusgabeEventKategorie kategorie;
    private final BigDecimal betrag;

    public AusgabeUpdatedEvent(Object source, String ausgabeId, LocalDate datum, String empfaenger, AusgabeEventKategorie kategorie, BigDecimal betrag) {
        super(source, Clock.systemUTC());
        this.ausgabeId = ausgabeId;
        this.datum = datum;
        this.empfaenger = empfaenger;
        this.kategorie = kategorie;
        this.betrag = betrag;
    }

    public boolean isKategorieForVergabung() {
        return this.kategorie == AusgabeEventKategorie.SPENDEN;
    }

    public boolean isKategorieForKosten() {
        if (this.kategorie == AusgabeEventKategorie.KRANKENKASSE) {
            return true;
        }
        return false;
    }
}
