package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.BerufBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.BerufServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.BerufPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class BerufService implements BerufServicePort {

    private final BerufPersistencePort berufPersistencePort;

    @Override
    @Transactional
    public String createBeruf(BerufBo source) {
        return berufPersistencePort.createBeruf(source);
    }

    @Override
    @Transactional(readOnly = true)
    public BerufBo getBerufById(String id) {
        return berufPersistencePort.getBerufById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BerufBo> getAllBeruf() {
        return berufPersistencePort.getAllBeruf();
    }

    @Override
    @Transactional
    public void updateBeruf(BerufBo source) {
        berufPersistencePort.updateBeruf(source);
    }

    @Override
    @Transactional
    public void deleteBerufById(String id) {
        berufPersistencePort.deleteBerufById(id);
    }
}
