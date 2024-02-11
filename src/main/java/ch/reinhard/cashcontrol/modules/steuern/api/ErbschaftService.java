package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface ErbschaftService {
    String createErbschaft(ErbschaftDto source);

    ErbschaftDto getErbschaftById(String id);

    List<ErbschaftDto> getAllErbschaft();

    void updateErbschaft(ErbschaftDto source);

    void deleteErbschaftById(String id);
}
