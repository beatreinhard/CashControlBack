package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AusgabeDeletedEventConsumerAdapter implements ApplicationListener<AusgabeDeletedEvent> {

    private final VergabungServicePort vergabungServicePort;

    @Override
    public void onApplicationEvent(AusgabeDeletedEvent event) {

        log.info("Consume AusgabeDeletedEvent: {}", event);
        //vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());
    }
}
