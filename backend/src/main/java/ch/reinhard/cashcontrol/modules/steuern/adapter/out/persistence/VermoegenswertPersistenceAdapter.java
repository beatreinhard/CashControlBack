package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VermoegenswertBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.VermoegenswertPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.VermoegenswertPersistenceMapper.*;

@Component
@RequiredArgsConstructor
public class VermoegenswertPersistenceAdapter implements VermoegenswertPersistencePort {

    private final VermoegenswertJpaRepository vermoegenswertJpaRepository;

    @Override
    public String createVermoegenswert(VermoegenswertBo source) {
        var vermoegenswert = toVermoegenswertEntity(source);
        vermoegenswert.setId(IdGenerator.generateId());
        var vermoegenswertEntity = vermoegenswertJpaRepository.save(vermoegenswert);
        return vermoegenswertEntity.getId();
    }

    @Override
    public VermoegenswertBo getVermoegenswertById(String id) {
        var entity = vermoegenswertJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vermoegenswert nicht gefunden mit ID=" + id));
        return toVermoegenswertBo(entity);
    }

    @Override
    public List<VermoegenswertBo> getAllVermoegenswert() {
        var vermoegenswertList = vermoegenswertJpaRepository.findAll();
        return toVermoegenswertBoList(vermoegenswertList);
    }

    @Override
    public void updateVermoegenswert(VermoegenswertBo source) {
        var vermoegenswertEntity = vermoegenswertJpaRepository
                .findById(source.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Vermoegenswert nicht gefunden mit ID=" + source.getId()));
        vermoegenswertEntity.update(toVermoegenswertEntity(source));
        vermoegenswertJpaRepository.save(vermoegenswertEntity);
    }

    @Override
    public void deleteVermoegenswertById(String id) {
        vermoegenswertJpaRepository.deleteById(id);
    }
}
