package ch.reinhard.cashcontrol.modules.steuern.adapter.in.domainevent;

import ch.reinhard.cashcontrol.core.domainevent.AusgabeUpdatedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AusgabeUpdatedEventConsumerAdapter implements ApplicationListener<AusgabeUpdatedEvent> {

    private final VergabungServicePort vergabungServicePort;

    @Override
    public void onApplicationEvent(AusgabeUpdatedEvent event) {
        log.info("Consume AusgabeUpdatedEvent: {}", event);

        // Mit ausgabeId die zugehörige Vergabung holen
        // var vergabung = vergabungServicePort.getVergabungByAusgabeId(event.getAusgabeId());

        //   - falls keine Vergabung existiert, und die AusgabeEventKategorie == SPENDEN, dann Vergabung erstellen
        // if (vergabung != null && AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
        vergabungServicePort.createVergabung(new VergabungBo());
        //

        //   - falls keine Vergabung existiert, und AusgabeEventKategorie != SPENDEN, dann nichts tun
        // if (vergabung != null && !AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {

        //   - falls eine Vergabung existiert, und AusgabeEventKategorie == SPENDEN, dann Vergabung updaten
        // if (vergabung != null && AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
         vergabungServicePort.updateVergabung(new VergabungBo());

         //   - falls eine Vergabung existiert, und AusgabeEventKategorie != SPENDEN, dann Vergabung löschen
        // if (vergabung != null && !AusgabeEventKategorie.SPENDEN.equals(event.getKategorie())) {
        //vergabungServicePort.deleteVergabungByAusgabeId(event.getAusgabeId());






    }
}
