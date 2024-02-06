package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.finanzen.api.*;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.JpaZahlungRepository;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.JpaZahlungViewRepository;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.QZahlungView;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.ZahlungEntity;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.finanzen.application.service.ZahlungEntityMapper.*;
import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class ZahlungServiceImpl implements ZahlungService {

    public final JpaZahlungRepository zahlungRepository;
    public final JpaZahlungViewRepository zahlungViewRepository;

    @Transactional
    public String createZahlung(ZahlungDetailsDto zahlungDetailsDto) {
        var zahlung = new ZahlungEntity(IdGenerator.generateId(), toZahlungEntityDetails(zahlungDetailsDto));
        var zahlungEntity = zahlungRepository.save(zahlung);
        return zahlungEntity.getId();
    }

    @Transactional(readOnly = true)
    public ZahlungDto getZahlungById(String id) {
        var zahlungEntity = zahlungRepository
                .findById(id)
                .orElseThrow(() -> new NoResultException("Zahlung nicht gefunden mit ID=" + id));
        return toZahlungDto(zahlungEntity);
    }

    @Transactional(readOnly = true)
    public List<ZahlungDto> getAllZahlung() {
        var zahlungList = zahlungRepository.findAll();
        return toZahlungDtoList(zahlungList);
    }

    @Transactional
    public void updateZahlung(ZahlungUpdateDto zahlungUpdateDto) {
        var zahlungEntity = zahlungRepository.findById(zahlungUpdateDto.id()).orElseThrow();
        validateUpdate(zahlungUpdateDto, zahlungEntity);
        zahlungEntity.update(toZahlungEntityDetails(zahlungUpdateDto.details()));
        zahlungRepository.save(zahlungEntity);
    }

    @Transactional
    public void deleteZahlungById(String id) {
        zahlungRepository.deleteById(id);
    }

    private void validateUpdate(ZahlungUpdateDto update, ZahlungEntity current) {
        if (current.getVersion() > update.version()) {
            throw new OptimisticLockingFailureException(format(
                    "Zahlung with id {0} could not be updated since it was mutated by someone else", update.id()));
        }
    }

    @Transactional(readOnly = true)
    public List<ZahlungViewDto> searchZahlungen(String empfaenger) {
        var zahlungViewQuery = QZahlungView.zahlungView;
        var booleanBuilder = new BooleanBuilder();
        if (empfaenger != null) {
            booleanBuilder.and(zahlungViewQuery.empfaenger.contains(empfaenger));
        }
        var predicate = booleanBuilder.getValue();

        assert booleanBuilder.getValue() != null;
        var zahlungViewIterable = zahlungViewRepository.findAll(predicate);

        return toZahlungViewDtoList(zahlungViewIterable);
    }
}
