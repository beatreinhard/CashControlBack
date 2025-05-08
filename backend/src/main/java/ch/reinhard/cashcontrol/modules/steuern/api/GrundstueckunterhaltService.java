package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface GrundstueckunterhaltService {
    String createGrundstueckunterhalt(GrundstueckunterhaltDto source);

    GrundstueckunterhaltDto getGrundstueckunterhaltById(String id);

    List<GrundstueckunterhaltDto> getAllGrundstueckunterhalt();

    void updateGrundstueckunterhalt(GrundstueckunterhaltDto source);

    void deleteGrundstueckunterhaltById(String id);
}
