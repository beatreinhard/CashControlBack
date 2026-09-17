package ch.reinhard.cashcontrol.modules.steuern.application.port.in;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.KostenViewBo;

import java.util.List;

public interface KostenServicePort {
    String createKosten(KostenBo source);

    KostenBo getKostenById(String id);

    List<KostenBo> getAllKosten();

    List<KostenBo> getKostenByJahr(Integer jahr);

    void updateKosten(KostenBo source);

    void deleteKostenById(String id);

    KostenBo getKostenByAusgabeId(String ausgabeId);

    void deleteKostenByAusgabeId(String ausgabeId);

    List<KostenViewBo> getKostenView();

    List<KostenViewBo> getKostenViewByJahr(Integer jahr);
}
