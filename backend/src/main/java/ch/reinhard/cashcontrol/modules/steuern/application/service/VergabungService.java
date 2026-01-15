package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.VergabungPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
class VergabungService implements VergabungServicePort {

    private final VergabungPersistencePort vergabungPersistencePort;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createVergabung(VergabungBo source) {
        return vergabungPersistencePort.createVergabung(source);
    }

    @Override
    @Transactional(readOnly = true)
    public VergabungBo getVergabungById(String id) {
        return vergabungPersistencePort.getVergabungById(id);
    }

    @Override
    public VergabungBo getVergabungByAusgabeId(String ausgabeId) {
        return vergabungPersistencePort.getVergabungByAusgabeId(ausgabeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VergabungBo> getAllVergabung() {
        return vergabungPersistencePort.getAllVergabung();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VergabungBo> getVergabungByJahr(Integer jahr) {
        return vergabungPersistencePort.getVergabungenByJahr(jahr);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateVergabung(VergabungBo source) {
        vergabungPersistencePort.updateVergabung(source);
    }

    @Override
    @Transactional
    public void deleteVergabungById(String id) {
        vergabungPersistencePort.deleteVergabungById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteVergabungByAusgabeId(String ausgabeId) {
        vergabungPersistencePort.deleteVergabungByAusgabeId(ausgabeId);
    }
}
