package ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;

import java.util.List;

public interface SchuldPersistencePort {
    String createSchuld(SchuldBo source);
    SchuldBo getSchuldById(String id);
    void updateSchuld(SchuldBo source);
    void deleteSchuldById(String id);
    List<SchuldBo> getAllSchuld();
    List<SchuldBo> getSchuldByJahr(Integer jahr);
}
