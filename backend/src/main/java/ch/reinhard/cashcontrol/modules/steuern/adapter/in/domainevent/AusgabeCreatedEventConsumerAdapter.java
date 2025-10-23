package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AusgabeCreatedEventConsumerAdapter implements ApplicationListener<AusgabeCreatedEvent> {

    private final VergabungServicePort vergabungServicePort;

    @Override
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        log.info("Consume AusgabeCreatedEvent: {}", event);

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
