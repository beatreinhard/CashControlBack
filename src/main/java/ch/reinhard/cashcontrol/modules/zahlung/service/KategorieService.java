package ch.reinhard.cashcontrol.modules.zahlung.service;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.persistence.JpaKategorieRepository;
import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api.KategorieDto;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class KategorieService {

    private final JpaKategorieRepository kategorieRepository;

//    @Transactional
//    public KategorieDto createKategorie(KategorieDto kategorieDto) {
//        var id = IdGenerator.generateId();
//        kategorieDto.id(id);
//        var kategorieEntity =  kategorieRepository.save(KategorieEntityMapper.toKategorieEntity(kategorieDto));
//        return KategorieEntityMapper.toKategorieDto(kategorieEntity);
//    }

    @Transactional(readOnly = true)
    public KategorieDto findKategorieById(String id) {
        var kategorieEntityOptional = kategorieRepository.findById(id);
        if (kategorieEntityOptional.isPresent()) {
            return KategorieEntityMapper.toKategorieDto(kategorieEntityOptional.get());
        }
        throw new NoResultException("Kategorie nicht gefunden mit ID="+id);
    }
}
