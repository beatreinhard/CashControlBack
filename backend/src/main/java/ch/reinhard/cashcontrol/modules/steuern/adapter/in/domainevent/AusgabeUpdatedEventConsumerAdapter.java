package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

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
public class AusgabeUpdatedEventConsumerAdapter {

    private final VergabungServicePort vergabungServicePort;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onApplicationEvent(AusgabeUpdatedEvent event) {
        log.info("Consume AusgabeUpdatedEvent for Category: {}", event.getKategorie());

        // Vergabung
        if (event.isKategorieForVergabung()) {
            // Mit ausgabeId die zugehörige Vergabung holen
            var vergabung = vergabungServicePort.getVergabungByAusgabeId(event.getAusgabeId());

            //   - falls keine Vergabung existiert, und die AusgabeEventKategorie == SPENDEN, dann Vergabung erstellen
            if (vergabung == null && AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
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

            //   - falls keine Vergabung existiert, und AusgabeEventKategorie != SPENDEN, dann nichts tun

            //   - falls eine Vergabung existiert, und AusgabeEventKategorie == SPENDEN, dann Vergabung updaten
            if (vergabung != null && AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
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

            //   - falls eine Vergabung existiert, und AusgabeEventKategorie != SPENDEN, dann Vergabung löschen
            if (vergabung != null && !AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
                log.info("Vergabung found with AusgbabeId and Event is not of SPENDEN, delete the Vergabung.");
                vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());
            }
        }

        // Kosten
        if (event.isKategorieForKosten()) {
            // TODO
        }
    }
}
