package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.api.KategorieDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.KategorieService;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.JpaKategorieRepository;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.KategorieEntity;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.application.service.KategorieEntityMapper.toKategorieDto;
import static ch.reinhard.cashcontrol.modules.finanzen.application.service.KategorieEntityMapper.toKategorieDtoList;
import static java.lang.String.format;

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
                .orElseThrow(() -> new NoResultException("Kategorie nicht gefunden mit ID=" + id));
        return toKategorieDto(kategorieEntity);
    }

    @Transactional(readOnly = true)
    public KategorieEntity getKategorieEntityById(String id) {
        var kategorieEntity = kategorieRepository
                .findById(id)
                .orElseThrow(() -> new NoResultException("Kategorie nicht gefunden mit ID=" + id));
        return kategorieEntity;
    }

    @Transactional(readOnly = true)
    public List<KategorieDto> getAllKategorie() {
        var kategorieList = kategorieRepository.findAll();
        return toKategorieDtoList(kategorieList);
    }

    @Transactional
    public void updateKategorie(KategorieDto kategorieDto) {
        var kategorieEntity = kategorieRepository.findById(kategorieDto.id()).orElseThrow();
        validateUpdate(kategorieDto, kategorieEntity);
        kategorieEntity.update(kategorieDto.bezeichnung());
        kategorieRepository.save(kategorieEntity);
    }

    @Transactional
    public void deleteKategorieById(String id) {
        kategorieRepository.deleteById(id);
    }

    private void validateUpdate(KategorieDto update, KategorieEntity current) {
        if (current.getVersion() > update.version()) {
            throw new OptimisticLockingFailureException(format(
                    "Kategorie with id {0} could not be updated since it was mutated by someone else", update.id()));
        }
    }
}
