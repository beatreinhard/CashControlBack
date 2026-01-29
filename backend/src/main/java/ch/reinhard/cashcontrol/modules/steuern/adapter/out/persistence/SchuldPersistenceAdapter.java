package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.SchuldPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.SchuldPersistenceMapper.*;


@Component
@RequiredArgsConstructor
public class SchuldPersistenceAdapter implements SchuldPersistencePort {

    private final SchuldJpaRepository schuldJpaRepository;

    @Override
    public String createSchuld(SchuldBo source) {
        var schuld = toSchuldEntity(source);
        schuld.setId(IdGenerator.generateId());
        var schuldEntity = schuldJpaRepository.save(schuld);
        return schuldEntity.getId();
    }

    @Override
    public SchuldBo getSchuldById(String id) {
        var entity = schuldJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schuld nicht gefunden mit ID=" + id));
        return toSchuldBo(entity);
    }

    @Override
    public void updateSchuld(SchuldBo source) {
        var schuldEntity = schuldJpaRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Schuld nicht gefunden mit ID=" + source.getId()));
        schuldEntity.update(toSchuldEntity(source));
        schuldJpaRepository.save(schuldEntity);
    }

    @Override
    public void deleteSchuldById(String id) {
        schuldJpaRepository.deleteById(id);
    }

    @Override
    public List<SchuldBo> getAllSchuld() {
        var schuldList = schuldJpaRepository.findAll();

        return toSchuldBoList(schuldList);
    }

    @Override
    public List<SchuldBo> getSchuldByJahr(Integer jahr) {
        var schuldList = schuldJpaRepository.findSchuldEntitiesByJahr(jahr);
        return toSchuldBoList(schuldList);
    }
}
