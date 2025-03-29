package ch.reinhard.cashcontrol.modules.finanzen.application.port.in;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;

import java.util.List;

public interface AusgabeServicePort {
    String createAusgabe(AusgabeBo source);
    AusgabeBo getAusgabeById(String id);
    List<AusgabeBo> getAllAusgabe();
    void updateAusgabe(AusgabeBo source);
    void deleteAusgabeById(String id);

}
