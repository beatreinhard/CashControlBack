package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertDto;
import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.JpaVermoegenswertRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.VermoegenswertMapper.*;

@RequiredArgsConstructor
@Service
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
        var entity = jpaVermoegenswertRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vermögenswert nicht gefunden mit ID=" + id));
        return toVermoegenswertDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VermoegenswertDto> getAllVermoegenswert() {
        var vermoegenswertList = jpaVermoegenswertRepository.findAll();
        return toVermoegenswertDtoList(vermoegenswertList);
    }

    @Override
    @Transactional
    public void updateVermoegenswert(VermoegenswertDto source) {
        var vermoegenswertEntity = jpaVermoegenswertRepository
                .findById(source.id())
                .orElseThrow(() -> new EntityNotFoundException("Vermoegenswert nicht gefunden mit ID=" + source.id()));
        vermoegenswertEntity.update(toVermoegenswert(source));
        jpaVermoegenswertRepository.save(vermoegenswertEntity);
    }

    @Override
    @Transactional
    public void deleteVermoegenswertById(String id) {
        jpaVermoegenswertRepository.deleteById(id);
    }
}
