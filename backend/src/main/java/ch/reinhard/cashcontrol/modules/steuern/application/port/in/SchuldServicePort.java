package ch.reinhard.cashcontrol.modules.steuern.application.port.in;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;

import java.util.List;

public interface SchuldServicePort {
    String createSchuld(SchuldBo source);

    SchuldBo getSchuldById(String id);

    List<SchuldBo> getAllSchuld();

    void updateSchuld(SchuldBo source);

    void deleteSchuldById(String id);
}
