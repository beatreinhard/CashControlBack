package ch.reinhard.cashcontrol.modules.steuern.application.port.in;



import ch.reinhard.cashcontrol.openapi.model.VergabungDto;

import java.util.List;

public interface VergabungServicePort {
    String createVergabung(VergabungDto source);

    VergabungDto getVergabungById(String id);

    List<VergabungDto> getAllVergabung();

    void updateVergabung(VergabungDto source);

    void deleteVergabungById(String id);
}
