package ch.reinhard.cashcontrol.modules.finanzen.application.port.in;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeViewBo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AusgabeServicePort {
    String createAusgabe(AusgabeBo source);
    AusgabeBo getAusgabeById(String id);
    List<AusgabeBo> getAllAusgabe();

    @Transactional(readOnly = true)
    List<AusgabeViewBo> getAllAusgabeView();

    void updateAusgabe(AusgabeBo source);
    void deleteAusgabeById(String id);

}
