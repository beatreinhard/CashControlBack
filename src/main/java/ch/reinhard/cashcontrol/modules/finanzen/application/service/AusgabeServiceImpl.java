package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.api.*;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.JpaAusgabeRepository;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.Ausgabe;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.core.persistence.OptimisticLockingValidator.validateOptimisticLocking;
import static ch.reinhard.cashcontrol.modules.finanzen.application.service.AusgabeMapper.*;

@RequiredArgsConstructor
@Service
public class AusgabeServiceImpl implements AusgabeService {

    public final JpaAusgabeRepository ausgabeRepository;

    @Transactional
    public String createAusgabe(AusgabeDto source) {
        var ausgabe = toAusgabe(source);
        ausgabe.setId(IdGenerator.generateId());
        var ausgabeEntity = ausgabeRepository.save(ausgabe);
        return ausgabeEntity.getId();
    }

    @Transactional(readOnly = true)
    public AusgabeDto getAusgabeById(String id) {
        var entity = ausgabeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ausgabe nicht gefunden mit ID=" + id));
        return toAusgabeDto(entity);
    }

    @Transactional(readOnly = true)
    public List<AusgabeDto> getAllAusgabe() {
        var ausgabeList = ausgabeRepository.findAll();
        return toAusgabeDtoList(ausgabeList);
    }

    @Transactional
    public void updateAusgabe(AusgabeDto source) {
        var ausgabeEntity = ausgabeRepository
                .findById(source.id())
                .orElseThrow(() -> new EntityNotFoundException("Ausgabe nicht gefunden mit ID=" + source.id()));
        validateOptimisticLocking(source.version(), ausgabeEntity.getVersion(), Ausgabe.class);
        ausgabeEntity.update(toAusgabe(source));
        ausgabeRepository.save(ausgabeEntity);
    }

    @Transactional
    public void deleteAusgabeById(String id) {
        ausgabeRepository.deleteById(id);
    }

}
