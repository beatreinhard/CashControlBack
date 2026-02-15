package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.core.service.EnumMapper;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import ch.reinhard.cashcontrol.openapi.model.KostenArtDto;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
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
    private final KostenService kostenService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        log.info("Consume AusgabeCreatedEvent for Category: {}", event.getKategorie());

        // Vergabung
        if (event.isKategorieForVergabung()) {
            VergabungBo vergabungBo = new VergabungBo()
                    .setId(null)
                    .setAusgabeId(event.getAusgabeId())
                    .setJahr(event.getDatum().getYear())
                    .setZahlungsDatum(event.getDatum())
                    .setEmpfaenger(event.getEmpfaenger())
                    .setBetrag(event.getBetrag());

            vergabungServicePort.createVergabung(vergabungBo);
        }

        // Kosten
        if (event.isKategorieForKosten()) {
            KostenDto kostenDto = new KostenDto()
                    .id(null)
                    .jahr(event.getDatum().getYear())
                    .art(EnumMapper.convert(event.getKategorie(), KostenArtDto.class))
                    .empfaenger(event.getEmpfaenger())
                    .zahlender(event.getZahlender())
                    .betrag(event.getBetrag())
                    .bemerkung(null);

            kostenService.createKosten(kostenDto);
        }

    }
}
