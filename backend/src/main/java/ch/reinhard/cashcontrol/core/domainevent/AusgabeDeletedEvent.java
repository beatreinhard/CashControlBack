package ch.reinhard.cashcontrol.core.domainevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class AusgabeDeletedEvent extends ApplicationEvent {
    private final String ausgabeId;
    private final AusgabeEventKategorie kategorie;

    public AusgabeDeletedEvent(Object source, String ausgabeId,  AusgabeEventKategorie kategorie) {
        super(source, Clock.systemUTC());
        this.ausgabeId = ausgabeId;
        this.kategorie = kategorie;
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
