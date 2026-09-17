package ch.reinhard.cashcontrol.modules.steuern.application.port.in;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.GrundstueckunterhaltBo;

import java.util.List;

public interface GrundstueckunterhaltServicePort {
    String createGrundstueckunterhalt(GrundstueckunterhaltBo source);

    GrundstueckunterhaltBo getGrundstueckunterhaltById(String id);

    List<GrundstueckunterhaltBo> getAllGrundstueckunterhalt();

    void updateGrundstueckunterhalt(GrundstueckunterhaltBo source);

    void deleteGrundstueckunterhaltById(String id);
}
