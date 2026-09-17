package ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.ErbschaftBo;

import java.util.List;

public interface ErbschaftPersistencePort {
    String createErbschaft(ErbschaftBo source);

    ErbschaftBo getErbschaftById(String id);

    List<ErbschaftBo> getAllErbschaft();

    void updateErbschaft(ErbschaftBo source);

    void deleteErbschaftById(String id);
}
