package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.core.persistence.IdGenerator;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldDto;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldService;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.JpaSchuldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.reinhard.cashcontrol.modules.steuern.application.service.SchuldMapper.toSchuld;

class SchuldServiceImpl implements SchuldService {
    @Autowired
    private JpaSchuldRepository jpaSchuldRepository;

    @Override
    @Transactional
    public String createSchuld(SchuldDto source) {
        var schuld = toSchuld(source);
        schuld.setId(IdGenerator.generateId());
        var schuldEntity = jpaSchuldRepository.save(schuld);
        return schuldEntity.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public SchuldDto getSchuldById(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchuldDto> getAllSchuld() {
        return null;
    }

    @Override
    @Transactional
    public void updateSchuld(SchuldDto source) {}

    @Override
    @Transactional
    public void deleteSchuldById(String id) {}
}
