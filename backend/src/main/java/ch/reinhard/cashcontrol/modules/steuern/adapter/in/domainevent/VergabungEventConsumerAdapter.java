package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeDeletedEvent;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeEventKategorie;
import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
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
public class VergabungEventConsumerAdapter {
    private final VergabungServicePort vergabungServicePort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeCreatedEvent event) {
        log.info("Consume AusgabeCreatedEvent for Category: {}", event.getKategorie());

        if (AusgabeEventKategorie.isKategorieForVergabung(event.getKategorie())) {
            VergabungBo vergabungBo = new VergabungBo()
                    .setId(null)
                    .setAusgabeId(event.getAusgabeId())
                    .setJahr(event.getDatum().getYear())
                    .setZahlungsDatum(event.getDatum())
                    .setEmpfaenger(event.getEmpfaenger())
                    .setBetrag(event.getBetrag());

            vergabungServicePort.createVergabung(vergabungBo);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeUpdatedEvent event) {
        log.info("Consume AusgabeUpdatedEvent for Category: {}", event.getKategorie());

        // Mit ausgabeId die zugehörige Vergabung holen
        var vergabung = vergabungServicePort.getVergabungByAusgabeId(event.getAusgabeId());

        if (AusgabeEventKategorie.isKategorieForVergabung(event.getKategorie())) {
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
        } else {
            //   - falls eine Vergabung existiert, und AusgabeEventKategorie != SPENDEN, dann Vergabung löschen
            if (vergabung != null && !AusgabeEventKategorie.isKategorieForVergabung(event.getKategorie())) {
                log.info("Vergabung found with AusgbabeId and Event is not of SPENDEN, delete the Vergabung.");
                vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());
            }
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeDeletedEvent event) {
        log.info("Consume AusgabeDeletedEvent for Category: {}", event.getKategorie());

        if (AusgabeEventKategorie.isKategorieForVergabung(event.getKategorie())) {
            vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());
        }

    }
}
