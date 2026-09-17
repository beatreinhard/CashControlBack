package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.ErbschaftServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.ErbschaftPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class ErbschaftService implements ErbschaftServicePort {

    private final ErbschaftPersistencePort erbschaftPersistencePort;

    @Override
    @Transactional
    public String createErbschaft(ErbschaftBo source) {
        return erbschaftPersistencePort.createErbschaft(source);
    }

    @Override
    @Transactional(readOnly = true)
    public ErbschaftBo getErbschaftById(String id) {
        return erbschaftPersistencePort.getErbschaftById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErbschaftBo> getAllErbschaft() {
        return erbschaftPersistencePort.getAllErbschaft();
    }

    @Override
    @Transactional
    public void updateErbschaft(ErbschaftBo source) {
        erbschaftPersistencePort.updateErbschaft(source);
    }

    @Override
    @Transactional
    public void deleteErbschaftById(String id) {
        erbschaftPersistencePort.deleteErbschaftById(id);
    }
}
