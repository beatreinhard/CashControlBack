package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.GrundstueckunterhaltBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.GrundstueckunterhaltServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.GrundstueckunterhaltPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class GrundstueckunterhaltService implements GrundstueckunterhaltServicePort {

    private final GrundstueckunterhaltPersistencePort grundstueckunterhaltPersistencePort;

    @Override
    @Transactional
    public String createGrundstueckunterhalt(GrundstueckunterhaltBo source) {
        return grundstueckunterhaltPersistencePort.createGrundstueckunterhalt(source);
    }

    @Override
    @Transactional(readOnly = true)
    public GrundstueckunterhaltBo getGrundstueckunterhaltById(String id) {
        return grundstueckunterhaltPersistencePort.getGrundstueckunterhaltById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrundstueckunterhaltBo> getAllGrundstueckunterhalt() {
        return grundstueckunterhaltPersistencePort.getAllGrundstueckunterhalt();
    }

    @Override
    @Transactional
    public void updateGrundstueckunterhalt(GrundstueckunterhaltBo source) {
        grundstueckunterhaltPersistencePort.updateGrundstueckunterhalt(source);
    }

    @Override
    @Transactional
    public void deleteGrundstueckunterhaltById(String id) {
        grundstueckunterhaltPersistencePort.deleteGrundstueckunterhaltById(id);
    }
}
