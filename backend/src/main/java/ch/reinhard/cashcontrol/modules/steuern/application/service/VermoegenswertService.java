package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VermoegenswertBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VermoegenswertServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.VermoegenswertPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class VermoegenswertService implements VermoegenswertServicePort {

    private final VermoegenswertPersistencePort vermoegenswertPersistencePort;

    @Override
    @Transactional
    public String createVermoegenswert(VermoegenswertBo source) {
        return vermoegenswertPersistencePort.createVermoegenswert(source);
    }

    @Override
    @Transactional(readOnly = true)
    public VermoegenswertBo getVermoegenswertById(String id) {
        return vermoegenswertPersistencePort.getVermoegenswertById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VermoegenswertBo> getAllVermoegenswert() {
        return vermoegenswertPersistencePort.getAllVermoegenswert();
    }

    @Override
    @Transactional
    public void updateVermoegenswert(VermoegenswertBo source) {
        vermoegenswertPersistencePort.updateVermoegenswert(source);
    }

    @Override
    @Transactional
    public void deleteVermoegenswertById(String id) {
        vermoegenswertPersistencePort.deleteVermoegenswertById(id);
    }
}
