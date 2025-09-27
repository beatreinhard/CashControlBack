package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.event.EventPort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.AusgabePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class AusgabeService implements AusgabeServicePort {

    private final AusgabePersistencePort ausgabePersistencePort;
    private final EventPort eventPort;


    // TODO müsste Transactional hier sein statt im PersistenceAdapter, da der Event sonst evtl. nicht publiziert wird, wenn die Transaktion im Adapter fehlschlägt
    //  siehe https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction-declarative-annotations
    //  und https://www.baeldung.com/spring-events
    //  und https://stackoverflow.com/questions/28997320/spring-transactional-events
    //  und https://stackoverflow.com/questions/30592503/spring-transaction
    public String createAusgabe(AusgabeBo source) {
        var id = ausgabePersistencePort.createAusgabe(source);

        // Publish event after creation
        eventPort.publishAusgabeCreatedEvent(source);

        return id;
    }

    public AusgabeBo getAusgabeById(String id) {
        return ausgabePersistencePort.getAusgabeById(id);
    }

    public List<AusgabeBo> getAllAusgabe() {
        return ausgabePersistencePort.getAllAusgabe();
    }

    public void updateAusgabe(AusgabeBo source) {
        ausgabePersistencePort.updateAusgabe(source);
    }

    public void deleteAusgabeById(String id) {
        ausgabePersistencePort.deleteAusgabeById(id);
    }

}
