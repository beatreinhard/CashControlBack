package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.BerufDto;
import ch.reinhard.cashcontrol.modules.steuern.api.BerufService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.Beruf;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaBerufRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.BerufMapper.*;
import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class BerufServiceImpl implements BerufService {

    private JpaBerufRepository jpaBerufRepository;

    @Transactional
    public String createBeruf(BerufDto source) {
        var beruf = toBeruf(source);
        beruf.setId(IdGenerator.generateId());
        var berufEntity = jpaBerufRepository.save(beruf);
        return berufEntity.getId();
    }

    @Transactional(readOnly = true)
    public BerufDto getBerufById(String id) {
        var berufEntity = jpaBerufRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Beruf nicht gefunden mit ID=" + id));
        return toBerufDto(berufEntity);
    }

    @Transactional(readOnly = true)
    public List<BerufDto> getAllBeruf() {
        var berufList = jpaBerufRepository.findAll();
        return toBerufDtoList(berufList);
    }

    @Transactional
    public void updateBeruf(BerufDto source) {
        var berufEntity = jpaBerufRepository
                .findById(source.id())
                .orElseThrow(() -> new EntityNotFoundException("Beruf nicht gefunden mit ID=" + source.id()));
        validateUpdate(source, berufEntity);
        berufEntity.update(toBeruf(source));
        jpaBerufRepository.save(berufEntity);
    }

    @Transactional
    public void deleteBerufById(String id) {
        jpaBerufRepository.deleteById(id);
    }

    // TODO evtl. in Core verschieben als Static-Methode
    private void validateUpdate(BerufDto update, Beruf current) {
        if (current.getVersion() > update.version()) {
            throw new OptimisticLockingFailureException(
                    format("Beruf with id {0} could not be updated since it was mutated by someone else", update.id()));
        }
    }
}
