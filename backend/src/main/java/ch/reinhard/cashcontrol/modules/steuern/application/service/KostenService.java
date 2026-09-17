package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenViewBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.KostenServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.KostenPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class KostenService implements KostenServicePort {

    private final KostenPersistencePort kostenPersistencePort;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createKosten(KostenBo source) {
        return kostenPersistencePort.createKosten(source);
    }

    @Override
    @Transactional(readOnly = true)
    public KostenBo getKostenById(String id) {
        return kostenPersistencePort.getKostenById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenBo> getAllKosten() {
        return kostenPersistencePort.getAllKosten();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenBo> getKostenByJahr(Integer jahr) {
        return kostenPersistencePort.getKostenByJahr(jahr);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateKosten(KostenBo source) {
        kostenPersistencePort.updateKosten(source);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteKostenById(String id) {
        kostenPersistencePort.deleteKostenById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public KostenBo getKostenByAusgabeId(String ausgabeId) {
        return kostenPersistencePort.getKostenByAusgabeId(ausgabeId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteKostenByAusgabeId(String ausgabeId) {
        kostenPersistencePort.deleteKostenByAusgabeId(ausgabeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenViewBo> getKostenView() {
        return kostenPersistencePort.getKostenView();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenViewBo> getKostenViewByJahr(Integer jahr) {
        return kostenPersistencePort.getKostenViewByJahr(jahr);
    }
}
