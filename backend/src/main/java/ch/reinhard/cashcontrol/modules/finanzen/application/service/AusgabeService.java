package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.domainevent.EventPort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.AusgabePersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AusgabeService implements AusgabeServicePort {

    private final AusgabePersistencePort ausgabePersistencePort;
    private final EventPort eventPort;


    // TODO async vs sync Event: https://www.baeldung.com/spring-events  https://chatgpt.com/c/68fccfbe-bd4c-832d-ab9f-572f34b8cb11
    @Override
    @Transactional
    public String createAusgabe(AusgabeBo source) {
        var id = ausgabePersistencePort.createAusgabe(source);
        source.setId(id);

        // Publish event after creation
        eventPort.publishAusgabeCreatedEvent(source);

        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public AusgabeBo getAusgabeById(String id) {
        return ausgabePersistencePort.getAusgabeById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AusgabeBo> getAllAusgabe() {
        return ausgabePersistencePort.getAllAusgabe();
    }

    @Override
    @Transactional
    public void updateAusgabe(AusgabeBo source) {
        ausgabePersistencePort.updateAusgabe(source);

        // Publish event after update
        eventPort.publishAusgabeUpdatedEvent(source);
    }

    @Override
    @Transactional
    public void deleteAusgabeById(String id) {
        ausgabePersistencePort.deleteAusgabeById(id);

        // Publish event after deletion
        eventPort.publishAusgabeDeletedEvent(id);
    }

}
