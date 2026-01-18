package ch.reinhard.cashcontrol.modules.steuern.adapter.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence.SchuldPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchuldPersistenceAdapter implements SchuldPersistencePort {

    private final SchuldJpaRepository schuldJpaRepository;

    @Override
    public String createSchuld(SchuldBo source) {
        return "";
    }

    @Override
    public SchuldBo getSchuldById(String id) {
        return null;
    }

    @Override
    public SchuldBo getSchuldByAusgabeId(String id) {
        return null;
    }

    @Override
    public void updateSchuld(SchuldBo source) {

    }

    @Override
    public void deleteSchuldById(String id) {
        schuldJpaRepository.deleteById(id);
    }

    @Override
    public List<SchuldBo> getAllSchuld() {
        return List.of();
    }

    @Override
    public List<SchuldBo> getSchuldByJahr(Integer jahr) {
        return List.of();
    }
}
