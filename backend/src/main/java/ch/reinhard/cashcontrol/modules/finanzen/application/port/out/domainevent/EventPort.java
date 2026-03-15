package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.domainevent;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeKategorieBo;

public interface EventPort {
    void publishAusgabeCreatedEvent(AusgabeBo ausgabeBo);
    void publishAusgabeDeletedEvent(String ausgabeId, AusgabeKategorieBo ausgabeKategorie);
    void publishAusgabeUpdatedEvent(AusgabeBo ausgabeBo);
}
