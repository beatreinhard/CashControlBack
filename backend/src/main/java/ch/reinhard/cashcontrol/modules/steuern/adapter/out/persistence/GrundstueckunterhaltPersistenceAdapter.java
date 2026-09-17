package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.GrundstueckunterhaltBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.GrundstueckunterhaltPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.GrundstueckunterhaltPersistenceMapper.*;

@Component
@RequiredArgsConstructor
public class GrundstueckunterhaltPersistenceAdapter implements GrundstueckunterhaltPersistencePort {

    private final GrundstueckunterhaltJpaRepository grundstueckunterhaltJpaRepository;

    @Override
    public String createGrundstueckunterhalt(GrundstueckunterhaltBo source) {
        var grundstueckunterhalt = toGrundstueckunterhaltEntity(source);
        grundstueckunterhalt.setId(IdGenerator.generateId());
        var grundstueckunterhaltEntity = grundstueckunterhaltJpaRepository.save(grundstueckunterhalt);
        return grundstueckunterhaltEntity.getId();
    }

    @Override
    public GrundstueckunterhaltBo getGrundstueckunterhaltById(String id) {
        var entity = grundstueckunterhaltJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grundstueckunterhalt nicht gefunden mit ID=" + id));
        return toGrundstueckunterhaltBo(entity);
    }

    @Override
    public List<GrundstueckunterhaltBo> getAllGrundstueckunterhalt() {
        var grundstueckunterhaltList = grundstueckunterhaltJpaRepository.findAll();
        return toGrundstueckunterhaltBoList(grundstueckunterhaltList);
    }

    @Override
    public void updateGrundstueckunterhalt(GrundstueckunterhaltBo source) {
        var grundstueckunterhaltEntity = grundstueckunterhaltJpaRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Grundstueckunterhalt nicht gefunden mit ID=" + source.getId()));
        grundstueckunterhaltEntity.update(toGrundstueckunterhaltEntity(source));
        grundstueckunterhaltJpaRepository.save(grundstueckunterhaltEntity);
    }

    @Override
    public void deleteGrundstueckunterhaltById(String id) {
        grundstueckunterhaltJpaRepository.deleteById(id);
    }
}
