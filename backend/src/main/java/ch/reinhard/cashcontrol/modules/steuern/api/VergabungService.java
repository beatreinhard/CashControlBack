package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface VergabungService {
    String createVergabung(VergabungDto source);

    VergabungDto getVergabungById(String id);

    List<VergabungDto> getAllVergabung();

    void updateVergabung(VergabungDto source);

    void deleteVergabungById(String id);
}
