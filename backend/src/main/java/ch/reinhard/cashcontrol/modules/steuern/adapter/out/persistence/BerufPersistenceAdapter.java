package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.BerufBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.BerufPersistencePort;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence.BerufPersistenceMapper.*;

@Component
public class BerufPersistenceAdapter implements BerufPersistencePort {

    @Autowired
    private BerufJpaRepository berufJpaRepository;

    @Override
    public String createBeruf(BerufBo source) {
        var beruf = toBerufEntity(source);
        beruf.setId(IdGenerator.generateId());
        var berufEntity = berufJpaRepository.save(beruf);
        return berufEntity.getId();
    }

    @Override
    public BerufBo getBerufById(String id) {
        var entity = berufJpaRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Beruf nicht gefunden mit ID=" + id));
        return toBerufBo(entity);
    }

    @Override
    public List<BerufBo> getAllBeruf() {
        var berufList = berufJpaRepository.findAll();
        return toBerufBoList(berufList);
    }

    @Override
    public void updateBeruf(BerufBo source) {
        var berufEntity = berufJpaRepository
                .findById(source.getId())
                .orElseThrow(() -> new EntityNotFoundException("Beruf nicht gefunden mit ID=" + source.getId()));
        berufEntity.update(toBerufEntity(source));
        berufJpaRepository.save(berufEntity);
    }

    @Override
    public void deleteBerufById(String id) {
        berufJpaRepository.deleteById(id);
    }
}
