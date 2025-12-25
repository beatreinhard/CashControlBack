package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltDto;
import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaGrundstueckunterhaltRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.GrundstueckunterhaltMapper.*;

@RequiredArgsConstructor
@Service
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
        var entity = jpaGrundstueckunterhaltRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grundstueckunterhalt nicht gefunden mit ID=" + id));
        return toGrundstueckunterhaltDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrundstueckunterhaltDto> getAllGrundstueckunterhalt() {
        var grundstueckunterhaltList = jpaGrundstueckunterhaltRepository.findAll();
        return toGrundstueckunterhaltDtoList(grundstueckunterhaltList);
    }

    @Override
    @Transactional
    public void updateGrundstueckunterhalt(GrundstueckunterhaltDto source) {
        var grundstueckunterhaltEntity = jpaGrundstueckunterhaltRepository
                .findById(source.id())
                .orElseThrow(
                        () -> new EntityNotFoundException("Grundstueckunterhalt nicht gefunden mit ID=" + source.id()));
        grundstueckunterhaltEntity.update(toGrundstueckunterhalt(source));
        jpaGrundstueckunterhaltRepository.save(grundstueckunterhaltEntity);
    }

    @Override
    @Transactional
    public void deleteGrundstueckunterhaltById(String id) {
        jpaGrundstueckunterhaltRepository.deleteById(id);
    }
}
