package ch.reinhard.cashcontrol.modules.steuern.application.service.TODO;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.TODO.JpaKostenRepository;
import ch.reinhard.cashcontrol.openapi.model.KostenDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.TODO.KostenMapper.*;

@RequiredArgsConstructor
@Service
class KostenServiceImpl implements KostenService {

    @Autowired
    private JpaKostenRepository jpaKostenRepository;

    @Override
    @Transactional
    public String createKosten(KostenDto source) {
        var kosten = toKosten(source);
        kosten.setId(IdGenerator.generateId());
        var kostenEntity = jpaKostenRepository.save(kosten);
        return kostenEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public KostenDto getKostenById(String id) {
        var entity = jpaKostenRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kosten nicht gefunden mit ID=" + id));
        return toKostenDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenDto> getAllKosten() {
        var kostenList = jpaKostenRepository.findAll();
        return toKostenDtoList(kostenList);
    }

    @Override
    public List<KostenDto> getKostenByJahr(Integer jahr) {
        var kostenList = jpaKostenRepository.findKostenByJahr(jahr);
        return toKostenDtoList(kostenList);
    }

    @Override
    @Transactional
    public void updateKosten(KostenDto source) {
        var kostenEntity = jpaKostenRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Kosten nicht gefunden mit ID=" + source.getId()));
        kostenEntity.update(toKosten(source));
        jpaKostenRepository.save(kostenEntity);
    }

    @Override
    @Transactional
    public void deleteKostenById(String id) {
        jpaKostenRepository.deleteById(id);
    }
}
