package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface VermoegenswertService {
    String createVermoegenswert(VermoegenswertDto source);

    VermoegenswertDto getVermoegenswertById(String id);

    List<VermoegenswertDto> getAllVermoegenswert();

    void updateVermoegenswert(VermoegenswertDto source);

    void deleteVermoegenswertById(String id);
}
