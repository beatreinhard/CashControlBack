package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeEventKategorie;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
import ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabeKategorie;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.domainevent.EventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static ch.reinhard.cashcontrol.core.service.EnumMapper.convert;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventPublisherAdapter implements EventPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishAusgabeCreatedEvent(AusgabeBo ausgabeBo) {
        var event = new AusgabeCreatedEvent(
                this,
                ausgabeBo.getId(),
                ausgabeBo.getDatum(),
                ausgabeBo.getZahlender(),
                ausgabeBo.getEmpfaenger(),
                convert(ausgabeBo.getKategorie(), AusgabeEventKategorie.class),
                ausgabeBo.getBetrag()
        );

        log.info("Ausgabe created event: {}", event);
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAusgabeDeletedEvent(String ausgabeId, AusgabeKategorie ausgabeKategorie) {
        var event = new AusgabeDeletedEvent(
                this,
                ausgabeId,
                convert(ausgabeKategorie, AusgabeEventKategorie.class)
        );

        // TODO log event
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAusgabeUpdatedEvent(AusgabeBo ausgabeBo) {
        var event = new AusgabeUpdatedEvent(
                this,
                ausgabeBo.getId(),
                ausgabeBo.getDatum(),
                ausgabeBo.getZahlender(),
                ausgabeBo.getEmpfaenger(),
                convert(ausgabeBo.getKategorie(), AusgabeEventKategorie.class),
                ausgabeBo.getBetrag()
        );

        // TODO log event
        applicationEventPublisher.publishEvent(event);
    }
}
