package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldDto;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Erbschaft;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaSchuldRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.steuern.application.service.SchuldMapper.*;

class SchuldServiceImpl implements SchuldService {
    @Autowired
    private JpaSchuldRepository jpaSchuldRepository;

    @Override
    @Transactional
    public String createSchuld(SchuldDto source) {
        var schuld = toSchuld(source);
        schuld.setId(IdGenerator.generateId());
        var schuldEntity = jpaSchuldRepository.save(schuld);
        return schuldEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public SchuldDto getSchuldById(String id) {
        var entity = jpaSchuldRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schuld nicht gefunden mit ID=" + id));
        return toSchuldDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchuldDto> getAllSchuld() {
        var schuldList = jpaSchuldRepository.findAll();
        return toSchuldDtoList(schuldList);
    }

    @Override
    @Transactional
    public void updateSchuld(SchuldDto source) {
        var schuldEntity = jpaSchuldRepository
                .findById(source.id())
                .orElseThrow(() -> new EntityNotFoundException("Schuld nicht gefunden mit ID=" + source.id()));
        validateOptimisticLocking(source.version(), schuldEntity.getVersion(), Erbschaft.class);
        schuldEntity.update(toSchuld(source));
        jpaSchuldRepository.save(schuldEntity);
    }

    @Override
    @Transactional
    public void deleteSchuldById(String id) {
        jpaSchuldRepository.deleteById(id);
    }
}
