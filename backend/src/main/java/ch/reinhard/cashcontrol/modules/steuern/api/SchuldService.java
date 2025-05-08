package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface SchuldService {
    String createSchuld(SchuldDto source);

    SchuldDto getSchuldById(String id);

    List<SchuldDto> getAllSchuld();

    void updateSchuld(SchuldDto source);

    void deleteSchuldById(String id);
}
