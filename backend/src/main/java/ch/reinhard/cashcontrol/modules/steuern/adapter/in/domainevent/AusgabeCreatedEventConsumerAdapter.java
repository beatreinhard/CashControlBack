package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class AusgabeCreatedEventConsumerAdapter {

    private final VergabungServicePort vergabungServicePort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        log.info("Consume AusgabeCreatedEvent: {}", event);

        if (!event.isKategorieSpenden()) {
            log.info("AusgabeCreatedEvent is not of category SPENDEN, ignoring.");
            return;
        }

        VergabungBo vergabungBo = new VergabungBo()
                .setId(null)
                .setVersion(null)
                .setAusgabeId(event.getAusgabeId())
                .setJahr(event.getDatum().getYear())
                .setZahlungsDatum(event.getDatum())
                .setEmpfaenger(event.getEmpfaenger())
                .setBetrag(event.getBetrag());

        vergabungServicePort.createVergabung(vergabungBo);
    }
}
