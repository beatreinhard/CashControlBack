package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftDto;
import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Erbschaft;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaErbschaftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.steuern.application.service.ErbschaftMapper.*;

@RequiredArgsConstructor
@Service
class ErbschaftServiceImpl implements ErbschaftService {

    @Autowired
    private JpaErbschaftRepository jpaErbschaftRepository;

    @Override
    @Transactional
    public String createErbschaft(ErbschaftDto source) {
        var erbschaft = toErbschaft(source);
        erbschaft.setId(IdGenerator.generateId());
        var erbschaftEntity = jpaErbschaftRepository.save(erbschaft);
        return erbschaftEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ErbschaftDto getErbschaftById(String id) {
        var entity = jpaErbschaftRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Erbschaft nicht gefunden mit ID=" + id));
        return toErbschaftDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErbschaftDto> getAllErbschaft() {
        var erbschaftList = jpaErbschaftRepository.findAll();
        return toErbschaftDtoList(erbschaftList);
    }

    @Override
    @Transactional
    public void updateErbschaft(ErbschaftDto source) {
        var erbschaftEntity = jpaErbschaftRepository
                .findById(source.id())
                .orElseThrow(() -> new EntityNotFoundException("Erbschaft nicht gefunden mit ID=" + source.id()));
        validateOptimisticLocking(source.version(), erbschaftEntity.getVersion(), Erbschaft.class);
        erbschaftEntity.update(toErbschaft(source));
        jpaErbschaftRepository.save(erbschaftEntity);
    }

    @Override
    @Transactional
    public void deleteErbschaftById(String id) {
        jpaErbschaftRepository.deleteById(id);
    }
}
