package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltDto;
import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaGrundstueckunterhaltRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.GrundstueckunterhaltMapper.toGrundstueckunterhalt;

public class GrundstueckunterhaltServiceImpl implements GrundstueckunterhaltService {

    @Autowired
    private JpaGrundstueckunterhaltRepository jpaGrundstueckunterhaltRepository;

    @Override
    @Transactional
    public String createGrundstueckunterhalt(GrundstueckunterhaltDto source) {
        var grundstueckunterhalt = toGrundstueckunterhalt(source);
        grundstueckunterhalt.setId(IdGenerator.generateId());
        var grundstueckunerhaltEntity = jpaGrundstueckunterhaltRepository.save(grundstueckunterhalt);
        return grundstueckunerhaltEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public GrundstueckunterhaltDto getGrundstueckunterhaltById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrundstueckunterhaltDto> getAllGrundstueckunterhalt() {
        return null;
    }

    @Override
    @Transactional
    public void updateGrundstueckunterhalt(GrundstueckunterhaltDto source) {}

    @Override
    @Transactional
    public void deleteGrundstueckunterhaltById(String id) {}
}
