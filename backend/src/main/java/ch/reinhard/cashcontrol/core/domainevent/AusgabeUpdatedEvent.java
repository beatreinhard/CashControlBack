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
    private final String zahlender;
    private final String empfaenger;
    private final String bemerkung;
    private final AusgabeEventKategorie kategorie;
    private final BigDecimal betrag;

    public AusgabeUpdatedEvent(Object source, String ausgabeId, LocalDate datum, String zahlender, String empfaenger, String bemerkung, AusgabeEventKategorie kategorie, BigDecimal betrag) {
        super(source, Clock.systemUTC());
        this.ausgabeId = ausgabeId;
        this.datum = datum;
        this.zahlender = zahlender;
        this.empfaenger = empfaenger;
        this.bemerkung = bemerkung;
        this.kategorie = kategorie;
        this.betrag = betrag;
    }
}
