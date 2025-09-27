package ch.reinhard.cashcontrol.core.domainevent;

import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

@Getter
public class AusgabeCreatedEvent extends ApplicationEvent {
    private final String id;
    private final LocalDate datum;
    private final String empfaenger;

    // TODO Kategorie als String speichern, damit das Event nicht von der Enumeration abhängt
    private final AusgabeKategorie kategorie;
    private final String text;
    private final BigDecimal betrag;


    public AusgabeCreatedEvent(Object source, Clock clock, String id, LocalDate datum, String empfaenger, AusgabeKategorie kategorie, String text, BigDecimal betrag) {
        super(source, clock);
        this.id = id;
        this.datum = datum;
        this.empfaenger = empfaenger;
        this.kategorie = kategorie;
        this.text = text;
        this.betrag = betrag;
    }
}
