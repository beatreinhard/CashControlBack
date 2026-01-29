package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.SchuldServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.SchuldPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
class SchuldService implements SchuldServicePort {

    private final SchuldPersistencePort schuldPersistencePort;

    @Override
    @Transactional
    public String createSchuld(SchuldBo source) {
        var id = schuldPersistencePort.createSchuld(source);
        source.setId(id);
        return id;
    }

    @Override
    @Transactional(readOnly = true)
    public SchuldBo getSchuldById(String id) {
        return schuldPersistencePort.getSchuldById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchuldBo> getAllSchuld() {
        return schuldPersistencePort.getAllSchuld();
    }

    @Override
    @Transactional
    public void updateSchuld(SchuldBo source) {
        schuldPersistencePort.updateSchuld(source);
    }

    @Override
    @Transactional
    public void deleteSchuldById(String id) {
        schuldPersistencePort.deleteSchuldById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchuldBo> getSchuldByJahr(Integer jahr) {
        return schuldPersistencePort.getSchuldByJahr(jahr);
    }
}
