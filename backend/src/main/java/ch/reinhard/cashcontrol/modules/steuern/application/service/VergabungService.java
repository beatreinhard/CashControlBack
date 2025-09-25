package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Erbschaft;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaVergabungRepository;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import ch.reinhard.cashcontrol.openapi.model.VergabungDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.steuern.application.service.VergabungMapper.*;

@RequiredArgsConstructor
@Service
class VergabungService implements VergabungServicePort {

    @Autowired
    private JpaVergabungRepository jpaVergabungRepository;

    @Override
    @Transactional
    public String createVergabung(VergabungDto source) {
        var vergabung = toVergabung(source);
        vergabung.setId(IdGenerator.generateId());
        var vergabungEntity = jpaVergabungRepository.save(vergabung);
        return vergabungEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public VergabungDto getVergabungById(String id) {
        var entity = jpaVergabungRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vergabung nicht gefunden mit ID=" + id));
        return toVergabungDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VergabungDto> getAllVergabung() {
        var vergabungList = jpaVergabungRepository.findAll();
        return toVergabungDtoList(vergabungList);
    }

    @Override
    @Transactional
    public void updateVergabung(VergabungDto source) {
        var vergabungEntity = jpaVergabungRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vergabung nicht gefunden mit ID=" + source.getId()));
        validateOptimisticLocking(source.getVersion(), vergabungEntity.getVersion(), Erbschaft.class);
        vergabungEntity.update(toVergabung(source));
        jpaVergabungRepository.save(vergabungEntity);
    }

    @Override
    @Transactional
    public void deleteVergabungById(String id) {
        jpaVergabungRepository.deleteById(id);
    }
}
