package ch.reinhard.cashcontrol.modules.steuern.application.service.TODO;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenJpaRepository;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.KostenViewJpaRepository;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
import ch.reinhard.cashcontrol.openapi.model.KostenViewDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.TODO.KostenMapper.*;

@RequiredArgsConstructor
@Service
class KostenServiceImpl implements KostenService {

    private final KostenJpaRepository kostenJpaRepository;
    private final KostenViewJpaRepository kostenViewJpaRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createKosten(KostenDto source) {
        var kosten = toKosten(source);
        kosten.setId(IdGenerator.generateId());
        var kostenEntity = kostenJpaRepository.save(kosten);
        return kostenEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public KostenDto getKostenById(String id) {
        var entity = kostenJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kosten nicht gefunden mit ID=" + id));
        return toKostenDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenDto> getAllKosten() {
        var kostenList = kostenJpaRepository.findAll();
        return toKostenDtoList(kostenList);
    }

    @Override
    public List<KostenDto> getKostenByJahr(Integer jahr) {
        var kostenList = kostenJpaRepository.findKostenByJahr(jahr);
        return toKostenDtoList(kostenList);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateKosten(KostenDto source) {
        var kostenEntity = kostenJpaRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Kosten nicht gefunden mit ID=" + source.getId()));
        kostenEntity.update(toKosten(source));
        kostenJpaRepository.save(kostenEntity);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteKostenById(String id) {
        kostenJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public KostenDto getKostenByAusgabeId(String ausgabeId) {
        var kosten = kostenJpaRepository.getKostenByAusgabeId(ausgabeId);
        return toKostenDto(kosten);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteKostgenByAusgabeId(String ausgabeId) {
        kostenJpaRepository.deleteKostenByAusgabeId(ausgabeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenViewDto> getKostenView() {
        var kostenList = kostenViewJpaRepository.findAll();
        return toKostenViewDtoList(kostenList);
    }

    @Override
    public List<KostenViewDto> getKostenViewByJahr(Integer jahr) {
        var kostenViewList = kostenViewJpaRepository.findKostenByJahr(jahr);
        return toKostenViewDtoList(kostenViewList);
    }
}
