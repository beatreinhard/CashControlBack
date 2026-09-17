package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenViewBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.KostenPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.KostenPersistenceMapper.*;

@Component
@RequiredArgsConstructor
public class KostenPersistenceAdapter implements KostenPersistencePort {

    private final KostenJpaRepository kostenJpaRepository;
    private final KostenViewJpaRepository kostenViewJpaRepository;

    @Override
    public String createKosten(KostenBo source) {
        var kosten = toKostenEntity(source);
        kosten.setId(IdGenerator.generateId());
        var kostenEntity = kostenJpaRepository.save(kosten);
        return kostenEntity.getId();
    }

    @Override
    public KostenBo getKostenById(String id) {
        var entity = kostenJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kosten nicht gefunden mit ID=" + id));
        return toKostenBo(entity);
    }

    @Override
    public List<KostenBo> getAllKosten() {
        var kostenList = kostenJpaRepository.findAll();
        return toKostenBoList(kostenList);
    }

    @Override
    public List<KostenBo> getKostenByJahr(Integer jahr) {
        var kostenList = kostenJpaRepository.findKostenByJahr(jahr);
        return toKostenBoList(kostenList);
    }

    @Override
    public void updateKosten(KostenBo source) {
        var kostenEntity = kostenJpaRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Kosten nicht gefunden mit ID=" + source.getId()));
        kostenEntity.update(toKostenEntity(source));
        kostenJpaRepository.save(kostenEntity);
    }

    @Override
    public void deleteKostenById(String id) {
        kostenJpaRepository.deleteById(id);
    }

    @Override
    public KostenBo getKostenByAusgabeId(String ausgabeId) {
        var kosten = kostenJpaRepository.getKostenByAusgabeId(ausgabeId);
        return toKostenBo(kosten);
    }

    @Override
    public void deleteKostenByAusgabeId(String ausgabeId) {
        kostenJpaRepository.deleteKostenByAusgabeId(ausgabeId);
    }

    @Override
    public List<KostenViewBo> getKostenView() {
        var kostenViewList = kostenViewJpaRepository.findAll();
        return toKostenViewBoList(kostenViewList);
    }

    @Override
    public List<KostenViewBo> getKostenViewByJahr(Integer jahr) {
        var kostenViewList = kostenViewJpaRepository.findKostenByJahr(jahr);
        return toKostenViewBoList(kostenViewList);
    }
}
