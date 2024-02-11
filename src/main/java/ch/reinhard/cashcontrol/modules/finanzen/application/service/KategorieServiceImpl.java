package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.api.KategorieDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.KategorieService;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.JpaKategorieRepository;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.KategorieEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.finanzen.application.service.KategorieEntityMapper.toKategorieDto;
import static ch.reinhard.cashcontrol.modules.finanzen.application.service.KategorieEntityMapper.toKategorieDtoList;

@RequiredArgsConstructor
@Service
public class KategorieServiceImpl implements KategorieService {

    private final JpaKategorieRepository kategorieRepository;

    @Transactional
    public String createKategorie(String bezeichnung) {
        var kategorie = new KategorieEntity(IdGenerator.generateId(), bezeichnung);
        var kategorieEntity = kategorieRepository.save(kategorie);
        return kategorieEntity.getId();
    }

    @Transactional(readOnly = true)
    public KategorieDto getKategorieById(String id) {
        var kategorieEntity = kategorieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategorie nicht gefunden mit ID=" + id));
        return toKategorieDto(kategorieEntity);
    }

    @Transactional(readOnly = true)
    public KategorieEntity getKategorieEntityById(String id) {
        var kategorieEntity = kategorieRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kategorie nicht gefunden mit ID=" + id));
        return kategorieEntity;
    }

    @Transactional(readOnly = true)
    public List<KategorieDto> getAllKategorie() {
        var kategorieList = kategorieRepository.findAll();
        return toKategorieDtoList(kategorieList);
    }

    @Transactional
    public void updateKategorie(KategorieDto source) {
        var kategorieEntity = kategorieRepository.findById(source.id()).orElseThrow();
        validateOptimisticLocking(source.version(), kategorieEntity.getVersion(), KategorieEntity.class);
        kategorieEntity.update(source.bezeichnung());
        kategorieRepository.save(kategorieEntity);
    }

    @Transactional
    public void deleteKategorieById(String id) {
        kategorieRepository.deleteById(id);
    }
}
