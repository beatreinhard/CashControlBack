package ch.reinhard.cashcontrol.modules.finanzen.api;

import java.util.List;

public interface ZahlungService {
    String createZahlung(ZahlungDetailsDto zahlungDetailsDto);
    ZahlungDto getZahlungById(String id);
    List<ZahlungDto> getAllZahlung();
    void updateZahlung(ZahlungUpdateDto zahlungUpdateDto);
    void deleteZahlungById(String id);
    List<ZahlungViewDto> searchZahlungen(String empfaenger);
}
