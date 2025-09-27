package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.event;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;

public interface EventPort {
    void publishAusgabeCreatedEvent(AusgabeBo ausgabeBo);
}
