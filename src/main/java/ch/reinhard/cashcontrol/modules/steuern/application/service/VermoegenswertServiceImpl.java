package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertDto;
import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaVermoegenswertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.VermoegenswertMapper.toVermoegenswert;

class VermoegenswertServiceImpl implements VermoegenswertService {
    @Autowired
    private JpaVermoegenswertRepository jpaVermoegenswertRepository;

    @Override
    @Transactional
    public String createVermoegenswert(VermoegenswertDto source) {
        var vermoegenswert = toVermoegenswert(source);
        vermoegenswert.setId(IdGenerator.generateId());
        var vermoegenswertEntity = jpaVermoegenswertRepository.save(vermoegenswert);
        return vermoegenswertEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public VermoegenswertDto getVermoegenswertById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VermoegenswertDto> getAllVermoegenswert() {
        return null;
    }

    @Override
    @Transactional
    public void updateVermoegenswert(VermoegenswertDto source) {}

    @Override
    @Transactional
    public void deleteVermoegenswertById(String id) {}
}
