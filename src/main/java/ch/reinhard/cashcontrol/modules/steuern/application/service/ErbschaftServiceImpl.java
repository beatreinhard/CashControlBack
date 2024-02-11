package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftDto;
import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaErbschaftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.ErbschaftMapper.toErbschaft;

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
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ErbschaftDto> getAllErbschaft() {
        return null;
    }

    @Override
    @Transactional
    public void updateErbschaft(ErbschaftDto source) {}

    @Override
    @Transactional
    public void deleteErbschaftById(String id) {}
}
