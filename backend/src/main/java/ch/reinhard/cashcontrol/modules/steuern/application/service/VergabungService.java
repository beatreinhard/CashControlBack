package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.VergabungPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class VergabungService implements VergabungServicePort {

    private final VergabungPersistencePort vergabungPersistencePort;

    @Override
    @Transactional
    public String createVergabung(VergabungBo source) {
        return vergabungPersistencePort.createVergabung(source);
    }

    @Override
    @Transactional(readOnly = true)
    public VergabungBo getVergabungById(String id) {
        return vergabungPersistencePort.getVergabungById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VergabungBo> getAllVergabung() {
        return vergabungPersistencePort.getAllVergabung();
    }

    @Override
    @Transactional
    public void updateVergabung(VergabungBo source) {
        vergabungPersistencePort.updateVergabung(source);
    }

    @Override
    @Transactional
    public void deleteVergabungById(String id) {
        vergabungPersistencePort.deleteVergabungById(id);
    }


//    @Override
//    public void consumeVergabungUpdatedEvent(Object event) {
//        // TODO hier sollten keine Events rein kommen, sondern nur AusgabeBo
//
//
//        AusgabeUpdatedEvent updatedEvent = (AusgabeUpdatedEvent) event;
//        var vergabungOdDb = vergabungPersistencePort.getVergabungByAusgabeId(updatedEvent.getAusgabeId());
//
//
//    }
//
//    @Override
//    public void consumeVergabungDeletedEvent(Object event) {
//        // TODO hier sollten keine Events rein kommen, sondern nur AusgabeBo
//
//        AusgabeDeletedEvent deletedEvent = (AusgabeDeletedEvent) event;
//        var vergabungOdDb = vergabungPersistencePort.getVergabungByAusgabeId(deletedEvent.getAusgabeId());
//        vergabungPersistencePort.deleteVergabungById(vergabungOdDb.getId());
//    }
}
