package ch.reinhard.cashcontrol.core.domainevent;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
public class AusgabeDeletedEvent extends ApplicationEvent {
    private final String ausgabeId;

    public AusgabeDeletedEvent(Object source, String ausgabeId) {
        super(source, Clock.systemUTC());
        this.ausgabeId = ausgabeId;
    }
}
