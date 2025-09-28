package ch.reinhard.cashcontrol.modules.steuern.adapter.in.event;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AusgabeCreatedEventConsumerAdapter implements ApplicationListener<AusgabeCreatedEvent> {

    private final VergabungServicePort vergabungServicePort;

    @Override
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        vergabungServicePort.consumeVergabungCreatedEvent(event);
    }
}
