package ch.reinhard.cashcontrol.modules.steuern.application.port.in;



import ch.reinhard.cashcontrol.core.domainevent.AusgabeCreatedEvent;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;

import java.util.List;

public interface VergabungServicePort {
    String createVergabung(VergabungBo source);

    VergabungBo getVergabungById(String id);

    List<VergabungBo> getAllVergabung();

    void updateVergabung(VergabungBo source);

    void deleteVergabungById(String id);



    void consumeVergabungCreatedEvent(AusgabeCreatedEvent event);

    void consumeVergabungUpdatedEvent(Object event);

    void consumeVergabungDeletedEvent(Object event);
}
