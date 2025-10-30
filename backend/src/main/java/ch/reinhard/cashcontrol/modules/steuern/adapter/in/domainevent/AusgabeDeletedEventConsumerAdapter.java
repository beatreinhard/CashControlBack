package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class AusgabeDeletedEventConsumerAdapter {

    private final VergabungServicePort vergabungServicePort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeDeletedEvent event) {

        log.info("Consume AusgabeDeletedEvent: {}", event);
        vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());
    }
}
