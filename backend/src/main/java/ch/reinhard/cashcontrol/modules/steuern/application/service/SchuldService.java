package ch.reinhard.cashcontrol.modules.steuern.application.service;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.SchuldServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.SchuldPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
class SchuldService implements SchuldServicePort {

    private final SchuldPersistencePort schuldPersistencePort;

    @Override
    public String createSchuld(SchuldBo source) {
        var id = schuldPersistencePort.createSchuld(source);
        source.setId(id);
        return id;
    }

    @Override
    public SchuldBo getSchuldById(String id) {
        return schuldPersistencePort.getSchuldById(id);
    }

    @Override
    public List<SchuldBo> getAllSchuld() {
        return schuldPersistencePort.getAllSchuld();
    }

    @Override
    public void updateSchuld(SchuldBo source) {
        schuldPersistencePort.updateSchuld(source);
    }

    @Override
    public void deleteSchuldById(String id) {
        schuldPersistencePort.deleteSchuldById(id);
    }

//    @Transactional
//    public String createSchuld(SchuldDto source) {
//        var schuld = toSchuld(source);
//        schuld.setId(IdGenerator.generateId());
//        var schuldEntity = schuldJpaRepository.save(schuld);
//        return schuldEntity.getId();
//    }
//
//    @Transactional(readOnly = true)
//    public SchuldDto getSchuldById(String id) {
//        var entity = schuldJpaRepository
//                .findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Schuld nicht gefunden mit ID=" + id));
//        return toSchuldDto(entity);
//    }
//
//    @Transactional(readOnly = true)
//    public List<SchuldDto> getAllSchuld() {
//        var schuldList = schuldJpaRepository.findAll();
//        return toSchuldDtoList(schuldList);
//    }
//
//    @Transactional
//    public void updateSchuld(SchuldDto source) {
//        var schuldEntity = schuldJpaRepository
//                .findById(source.id())
//                .orElseThrow(() -> new EntityNotFoundException("Schuld nicht gefunden mit ID=" + source.id()));
//        schuldEntity.update(toSchuld(source));
//        schuldJpaRepository.save(schuldEntity);
//    }
//
//    @Transactional
//    public void deleteSchuldById(String id) {
//        schuldJpaRepository.deleteById(id);
//    }
}
