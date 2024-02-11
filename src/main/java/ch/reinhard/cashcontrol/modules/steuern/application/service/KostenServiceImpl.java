package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenDto;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaKostenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.KostenMapper.toKosten;

class KostenServiceImpl implements KostenService {

    @Autowired
    private JpaKostenRepository jpaKostenRepository;

    @Override
    @Transactional
    public String createKosten(KostenDto source) {
        var kosten = toKosten(source);
        kosten.setId(IdGenerator.generateId());
        var kostenEntity = jpaKostenRepository.save(kosten);
        return kostenEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public KostenDto getKostenById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<KostenDto> getAllKosten() {
        return null;
    }

    @Override
    @Transactional
    public void updateKosten(KostenDto source) {}

    @Override
    @Transactional
    public void deleteKostenById(String id) {}
}
