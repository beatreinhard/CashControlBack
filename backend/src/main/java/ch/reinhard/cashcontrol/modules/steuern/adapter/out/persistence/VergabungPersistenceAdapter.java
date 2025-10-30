package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Erbschaft;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.VergabungPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.VergabungPersistenceMapper.*;

@Component
public class VergabungPersistenceAdapter implements VergabungPersistencePort {


    @Autowired
    private JpaVergabungRepository jpaVergabungRepository;

    @Override
    public String createVergabung(VergabungBo source) {
        var vergabung = toVergabung(source);
        vergabung.setId(IdGenerator.generateId());
        var vergabungEntity = jpaVergabungRepository.save(vergabung);
        return vergabungEntity.getId();
    }

    @Override
    public VergabungBo getVergabungById(String id) {
        var entity = jpaVergabungRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vergabung nicht gefunden mit ID=" + id));
        return toVergabungBo(entity);
    }

    @Override
    public VergabungBo getVergabungByAusgabeId(String id) {
        var entity = jpaVergabungRepository
                .findByAusgabeId(id);
        return toVergabungBo(entity);
    }

    @Override
    public List<VergabungBo> getAllVergabung() {
        var vergabungList = jpaVergabungRepository.findAll();
        return toVergabungBoList(vergabungList);
    }

    @Override
    public void updateVergabung(VergabungBo source) {
        var vergabungEntity = jpaVergabungRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Vergabung nicht gefunden mit ID=" + source.getId()));
        validateOptimisticLocking(source.getVersion(), vergabungEntity.getVersion(), Erbschaft.class);
        vergabungEntity.update(toVergabung(source));
        jpaVergabungRepository.save(vergabungEntity);
    }

    @Override
    public void deleteVergabungById(String id) {
        jpaVergabungRepository.deleteById(id);
    }

    @Override
    public void deleteVergabungByAusgabeId(String ausgabeId) {
        jpaVergabungRepository.deleteVergabungByAusgabeId(ausgabeId);
    }
}
