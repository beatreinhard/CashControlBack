package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.domainevent;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;

public interface EventPort {
    void publishAusgabeCreatedEvent(AusgabeBo ausgabeBo);
    void publishAusgabeDeletedEvent(String ausgabeId);
    void publishAusgabeUpdatedEvent(AusgabeBo ausgabeBo);
}
