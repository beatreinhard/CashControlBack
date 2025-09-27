package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.event;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.event.EventPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventAdapter implements EventPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishAusgabeCreatedEvent(AusgabeBo ausgabeBo) {

        // create Event
        var event = new AusgabeCreatedEvent(
                this,
                java.time.Clock.systemUTC(),
                ausgabeBo.id(),
                ausgabeBo.datum(),
                ausgabeBo.empfaenger(),
                ausgabeBo.kategorie(),
                ausgabeBo.text(),
                ausgabeBo.betrag()
        );
        // TODO log event
        applicationEventPublisher.publishEvent(event);
    }
}
