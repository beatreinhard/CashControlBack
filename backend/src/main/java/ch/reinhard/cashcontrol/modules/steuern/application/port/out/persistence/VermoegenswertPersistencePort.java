package ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VermoegenswertBo;

import java.util.List;

public interface VermoegenswertPersistencePort {
    String createVermoegenswert(VermoegenswertBo source);

    VermoegenswertBo getVermoegenswertById(String id);

    List<VermoegenswertBo> getAllVermoegenswert();

    void updateVermoegenswert(VermoegenswertBo source);

    void deleteVermoegenswertById(String id);
}
