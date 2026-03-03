package ch.reinhard.cashcontrol.modules.finanzen.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeViewBo;

import java.util.List;

public interface AusgabePersistencePort {

    AusgabeBo getAusgabeById(String id);
    List<AusgabeBo> getAllAusgabe();
    String createAusgabe(AusgabeBo source);
    void updateAusgabe(AusgabeBo source);
    void deleteAusgabeById(String id);
    List<AusgabeViewBo> getAllAusgabeView();

}
