package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence.AusgabePersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.finanzen.adapter.out.persistence.AusgabePersistenceMapper.*;

@RequiredArgsConstructor
@Service
public class AusgabePersistenceAdapter implements AusgabePersistencePort {

    private final JpaAusgabeRepository ausgabeRepository;

    @Transactional
    @Override
    public String createAusgabe(AusgabeBo source) {
        var ausgabe = toAusgabe(source);
        ausgabe.setId(IdGenerator.generateId());
        var ausgabeEntity = ausgabeRepository.save(ausgabe);
        return ausgabeEntity.getId();
    }

    @Transactional(readOnly = true)
    @Override
    public AusgabeBo getAusgabeById(String id) {
        var entity = ausgabeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ausgabe nicht gefunden mit ID=" + id));
        return toAusgabeBo(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AusgabeBo> getAllAusgabe() {
        var ausgabeList = ausgabeRepository.findAll();

        return toAusgabeBoList(ausgabeList);
    }


    @Transactional
    @Override
    public void updateAusgabe(AusgabeBo source) {
        var ausgabeEntity = ausgabeRepository
                .findById(source.id())
                .orElseThrow(() -> new EntityNotFoundException("Ausgabe nicht gefunden mit ID=" + source.id()));
        validateOptimisticLocking(source.version(), ausgabeEntity.getVersion(), AusgabeEntity.class);
        ausgabeEntity.update(toAusgabe(source));
        ausgabeRepository.save(ausgabeEntity);
    }

    @Transactional
    @Override
    public void deleteAusgabeById(String id) {
        ausgabeRepository.deleteById(id);
    }
}
