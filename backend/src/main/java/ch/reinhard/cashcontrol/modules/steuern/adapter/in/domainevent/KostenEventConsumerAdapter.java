package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeEventKategorie;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
import ch.reinhard.cashcontrol.core.service.EnumMapper;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
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
public class KostenEventConsumerAdapter {

    private final KostenService kostenService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        log.info("Consume AusgabeCreatedEvent for Category: {}", event.getKategorie());

        if (AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
            KostenDto kostenDto = new KostenDto()
                    .id(null)
                    .ausgabeId(event.getAusgabeId())
                    .jahr(event.getDatum().getYear())
                    .art(EnumMapper.convert(event.getKategorie(), KostenArtDto.class))
                    .empfaenger(event.getEmpfaenger())
                    .zahlender(event.getZahlender())
                    .betrag(event.getBetrag())
                    .bemerkung(event.getBemerkung());

            kostenService.createKosten(kostenDto);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeUpdatedEvent event) {
        log.info("Consume AusgabeUpdatedEvent for Category: {}", event.getKategorie());

        // Mit ausgabeId die zugehörige Kosten holen
        var kosten = kostenService.getKostenByAusgabeId(event.getAusgabeId());

        if (AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
            //   - falls keine Kosten existiert und EventKategorie gehört zu Kosten, dann Kosten erstellen
            if (kosten == null) {
                log.info("No Kosten found with AusgbabeId and Event is for KOSTEN, create a new Kosten.");

                KostenDto kostenDto = new KostenDto()
                        .id(null)
                        .ausgabeId(event.getAusgabeId())
                        .jahr(event.getDatum().getYear())
                        .art(EnumMapper.convert(event.getKategorie(), KostenArtDto.class))
                        .empfaenger(event.getEmpfaenger())
                        .zahlender(event.getZahlender())
                        .betrag(event.getBetrag())
                        .bemerkung(null);

                kostenService.createKosten(kostenDto);
            }

            //   - falls Kosten existiert und EventKategorie gehört zu Kosten, dann Kosten updaten
            if (kosten != null) {
                log.info("Kosten found with AusgbabeId and Event is for KOSTEN, update the Kosten.");

                KostenDto kostenDto = new KostenDto()
                        .id(kosten.getId())
                        .ausgabeId(event.getAusgabeId())
                        .jahr(event.getDatum().getYear())
                        .art(EnumMapper.convert(event.getKategorie(), KostenArtDto.class))
                        .empfaenger(event.getEmpfaenger())
                        .zahlender(event.getZahlender())
                        .betrag(event.getBetrag())
                        .bemerkung(event.getBemerkung());

                kostenService.updateKosten(kostenDto);
            }
        } else {
            //   - falls Kosten existiert, und AusgabeEventKategorie != KOSTEN, dann Kosten löschen
            if (kosten != null && !AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
                log.info("Kosten found with AusgbabeId and Event is not of KOSTEN, delete the Kosten.");
                kostenService.deleteKostgenByAusgabeId(event.getAusgabeId());
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeDeletedEvent event) {
        log.info("Consume AusgabeDeletedEvent for Category: {}", event.getKategorie());

        if (AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
            kostenService.deleteKostgenByAusgabeId(event.getAusgabeId());
        }
    }
}
