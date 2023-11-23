package ch.reinhard.cashcontrol.modules.zahlung.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.zahlung.domain.JpaZahlungRepository;
import ch.reinhard.cashcontrol.modules.zahlung.domain.JpaZahlungViewRepository;
import ch.reinhard.cashcontrol.modules.zahlung.domain.QZahlungEntity;
import ch.reinhard.cashcontrol.modules.zahlung.domain.ZahlungEntity;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungDto;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungUpdateDto;
import ch.reinhard.cashcontrol.modules.zahlung.service.api.ZahlungViewDto;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.zahlung.service.ZahlungEntityMapper.*;
import static java.lang.String.format;


@RequiredArgsConstructor
@Service
public class ZahlungService {

    public final JpaZahlungRepository zahlungRepository;
    public final JpaZahlungViewRepository zahlungViewRepository;

    @Transactional
    public String createZahlung(ZahlungDetailsDto zahlungDetailsDto) {
        var zahlung = new ZahlungEntity(IdGenerator.generateId(), toZahlungEntityDetails(zahlungDetailsDto));
        var zahlungEntity =  zahlungRepository.save(zahlung);
        return zahlungEntity.getId();
    }

    @Transactional(readOnly = true)
    public ZahlungDto getZahlungById(String id) {
        var zahlungEntity =  zahlungRepository.findById(id).orElseThrow(() -> new NoResultException("Zahlung nicht gefunden mit ID="+id));
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
            throw new OptimisticLockingFailureException(
                    format("Zahlung with id {0} could not be updated since it was mutated by someone else", update.id()));
        }
    }

    @Transactional(readOnly = true)
    public List<ZahlungDto> searchZahlungen(String empfaenger) {

        var zahlungQuery = QZahlungEntity.zahlungEntity;
        var booleanBuilder = new BooleanBuilder();
        if (empfaenger != null) {
            booleanBuilder.and(zahlungQuery.empfaenger.contains(empfaenger));
        }
        var predicate = booleanBuilder.getValue();

        assert booleanBuilder.getValue() != null;
        var zahlungEntityIterable = zahlungRepository.findAll(predicate);

        return toZahlungDtoList(zahlungEntityIterable);
    }

    @Transactional(readOnly = true)
    public List<ZahlungViewDto> searchZahlungen() {
        var zahlungViewList = zahlungViewRepository.findAll();
        return toZahlungViewDtoList(zahlungViewList);
    }

}
