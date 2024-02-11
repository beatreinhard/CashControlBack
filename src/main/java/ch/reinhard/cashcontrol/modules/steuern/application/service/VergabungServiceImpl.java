package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.VergabungDto;
import ch.reinhard.cashcontrol.modules.steuern.api.VergabungService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaVergabungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.VergabungMapper.toVergabung;

class VergabungServiceImpl implements VergabungService {

    @Autowired
    private JpaVergabungRepository jpaVergabungRepository;

    @Override
    @Transactional
    public String createVergabung(VergabungDto source) {
        var vergabung = toVergabung(source);
        vergabung.setId(IdGenerator.generateId());
        var vergabungEntity = jpaVergabungRepository.save(vergabung);
        return vergabungEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public VergabungDto getVergabungById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VergabungDto> getAllVergabung() {
        return null;
    }

    @Override
    @Transactional
    public void updateVergabung(VergabungDto source) {}

    @Override
    @Transactional
    public void deleteVergabungById(String id) {}
}
