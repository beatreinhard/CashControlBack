package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeEventKategorie;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
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
public class AusgabeUpdatedEventConsumerAdapter {

    private final VergabungServicePort vergabungServicePort;
    private final KostenService kostenService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeUpdatedEvent event) {
        log.info("Consume AusgabeUpdatedEvent for Category: {}", event.getKategorie());

        // Vergabung
        if (event.isKategorieForVergabung()) {
            // Mit ausgabeId die zugehörige Vergabung holen
            var vergabung = vergabungServicePort.getVergabungByAusgabeId(event.getAusgabeId());

            //   - falls keine Vergabung existiert, und die AusgabeEventKategorie == SPENDEN, dann Vergabung erstellen
            if (vergabung == null) {
                log.info("No Vergabung found with AusgbabeId and Event is not of SPENDEN, create a new Vergabung.");

                VergabungBo vergabungBo = new VergabungBo()
                        .setId(null)
                        .setAusgabeId(event.getAusgabeId())
                        .setJahr(event.getDatum().getYear())
                        .setZahlungsDatum(event.getDatum())
                        .setEmpfaenger(event.getEmpfaenger())
                        .setBetrag(event.getBetrag());

                vergabungServicePort.createVergabung(vergabungBo);
            }

            //   - falls eine Vergabung existiert, und AusgabeEventKategorie == SPENDEN, dann Vergabung updaten
            if (vergabung != null) {
                log.info("Vergabung found with AusgbabeId and Event is of SPENDEN, update the Vergabung.");

                VergabungBo vergabungBo = new VergabungBo()
                        .setId(vergabung.getId())
                        .setAusgabeId(event.getAusgabeId())
                        .setJahr(event.getDatum().getYear())
                        .setZahlungsDatum(event.getDatum())
                        .setEmpfaenger(event.getEmpfaenger())
                        .setBetrag(event.getBetrag());

                vergabungServicePort.updateVergabung(vergabungBo);
            }


            // TODO dies muss separat gelöst werden, da wir hier innerhalb event.kategorie == Spenden sind (siehe If ganz oben)

            //   - falls eine Vergabung existiert, und AusgabeEventKategorie != SPENDEN, dann Vergabung löschen
            if (vergabung != null && !AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
                log.info("Vergabung found with AusgbabeId and Event is not of SPENDEN, delete the Vergabung.");
                vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());
            }
        }

        // Kosten
        if (event.isKategorieForKosten()) {
            // Mit ausgabeId die zugehörige Kosten holen
            var kosten = kostenService.getKostenByAusgabeId(event.getAusgabeId());

            //   - falls keine Kosten existiert und EventKategorie gehört zu Kosten, dann Kosten erstellen
            if (kosten == null) {
                log.info("No Kosten found with AusgbabeId and Event is for KOSTEN, create a new Vergabung.");

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

            //   - falls Kosten existiert und EventKategorie gehört zu Kosten, dann Kosten updaten
            if (kosten != null) {
                log.info("Kosten found with AusgbabeId and Event is for KOSTEN, update the Vergabung.");

                KostenDto kostenDto = new KostenDto()
                        .id(kosten.getId())
                        .jahr(event.getDatum().getYear())
                        .art(EnumMapper.convert(event.getKategorie(), KostenArtDto.class))
                        .empfaenger(event.getEmpfaenger())
                        .zahlender(event.getZahlender())
                        .betrag(event.getBetrag())
                        .bemerkung(event.getBemerkung());

                kostenService.updateKosten(kostenDto);
            }

            // TODO löschen muss separat gelöst werden, da wir hier innerhalb event.kategorie == Spenden sind (siehe If ganz oben)
        }
    }
}
