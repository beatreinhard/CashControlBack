package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.ErbschaftPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.ErbschaftPersistenceMapper.*;

@Component
@RequiredArgsConstructor
public class ErbschaftPersistenceAdapter implements ErbschaftPersistencePort {

    private final ErbschaftJpaRepository erbschaftJpaRepository;

    @Override
    public String createErbschaft(ErbschaftBo source) {
        var erbschaft = toErbschaftEntity(source);
        erbschaft.setId(IdGenerator.generateId());
        var erbschaftEntity = erbschaftJpaRepository.save(erbschaft);
        return erbschaftEntity.getId();
    }

    @Override
    public ErbschaftBo getErbschaftById(String id) {
        var entity = erbschaftJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Erbschaft nicht gefunden mit ID=" + id));
        return toErbschaftBo(entity);
    }

    @Override
    public List<ErbschaftBo> getAllErbschaft() {
        var erbschaftList = erbschaftJpaRepository.findAll();
        return toErbschaftBoList(erbschaftList);
    }

    @Override
    public void updateErbschaft(ErbschaftBo source) {
        var erbschaftEntity = erbschaftJpaRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Erbschaft nicht gefunden mit ID=" + source.getId()));
        erbschaftEntity.update(toErbschaftEntity(source));
        erbschaftJpaRepository.save(erbschaftEntity);
    }

    @Override
    public void deleteErbschaftById(String id) {
        erbschaftJpaRepository.deleteById(id);
    }
}
