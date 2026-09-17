package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeEventKategorie;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
import ch.reinhard.cashcontrol.core.service.EnumMapper;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.KostenServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class KostenEventConsumerAdapter {

    private final KostenServicePort kostenServicePort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        log.info("Consume AusgabeCreatedEvent for Category: {}", event.getKategorie());

        if (AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
            var zahlender = event.getZahlender();
            if (zahlender == null || zahlender.isEmpty()) {
                log.warn(
                        "Zahlender is null or empty for AusgabeCreatedEvent with Kategorie: {}. Setting zahlender to 'Unbekannt'.",
                        event.getKategorie());
                zahlender = "Unbekannt";
            }

            KostenBo kostenBo = new KostenBo()
                    .setId(null)
                    .setAusgabeId(event.getAusgabeId())
                    .setJahr(event.getDatum().getYear())
                    .setArt(EnumMapper.convert(event.getKategorie(), KostenArtBo.class))
                    .setEmpfaenger(event.getEmpfaenger())
                    .setZahlender(zahlender)
                    .setBetrag(event.getBetrag())
                    .setBemerkung(event.getBemerkung());

            kostenServicePort.createKosten(kostenBo);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeUpdatedEvent event) {
        log.info("Consume AusgabeUpdatedEvent for Category: {}", event.getKategorie());

        // Mit ausgabeId die zugehörige Kosten holen
        var kosten = kostenServicePort.getKostenByAusgabeId(event.getAusgabeId());

        if (AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
            var zahlender = event.getZahlender();
            if (zahlender == null || zahlender.isEmpty()) {
                log.warn(
                        "Zahlender is null or empty for AusgabeCreatedEvent with Kategorie: {}. Setting zahlender to 'Unbekannt'.",
                        event.getKategorie());
                zahlender = "Unbekannt";
            }
            //   - falls keine Kosten existiert und EventKategorie gehört zu Kosten, dann Kosten erstellen
            if (kosten == null) {
                log.info("No Kosten found with AusgbabeId and Event is for KOSTEN, create a new Kosten.");

                KostenBo kostenBo = new KostenBo()
                        .setId(null)
                        .setAusgabeId(event.getAusgabeId())
                        .setJahr(event.getDatum().getYear())
                        .setArt(EnumMapper.convert(event.getKategorie(), KostenArtBo.class))
                        .setEmpfaenger(event.getEmpfaenger())
                        .setZahlender(zahlender)
                        .setBetrag(event.getBetrag())
                        .setBemerkung(null);

                kostenServicePort.createKosten(kostenBo);
            }

            //   - falls Kosten existiert und EventKategorie gehört zu Kosten, dann Kosten updaten
            if (kosten != null) {
                log.info("Kosten found with AusgbabeId and Event is for KOSTEN, update the Kosten.");

                KostenBo kostenBo = new KostenBo()
                        .setId(kosten.getId())
                        .setAusgabeId(event.getAusgabeId())
                        .setJahr(event.getDatum().getYear())
                        .setArt(EnumMapper.convert(event.getKategorie(), KostenArtBo.class))
                        .setEmpfaenger(event.getEmpfaenger())
                        .setZahlender(zahlender)
                        .setBetrag(event.getBetrag())
                        .setBemerkung(event.getBemerkung());

                kostenServicePort.updateKosten(kostenBo);
            }
        } else {
            //   - falls Kosten existiert, und AusgabeEventKategorie != KOSTEN, dann Kosten löschen
            if (kosten != null && !AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
                log.info("Kosten found with AusgbabeId and Event is not of KOSTEN, delete the Kosten.");
                kostenServicePort.deleteKostenByAusgabeId(event.getAusgabeId());
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeDeletedEvent event) {
        log.info("Consume AusgabeDeletedEvent for Category: {}", event.getKategorie());

        if (AusgabeEventKategorie.isKategorieForKosten(event.getKategorie())) {
            kostenServicePort.deleteKostenByAusgabeId(event.getAusgabeId());
        }
    }
}
