package ch.reinhard.cashcontrol.modules.finanzen.api;

import java.util.List;

public interface AusgabeService {
    String createAusgabe(AusgabeDto source);
    AusgabeDto getAusgabeById(String id);
    List<AusgabeDto> getAllAusgabe();
    void updateAusgabe(AusgabeDto source);
    void deleteAusgabeById(String id);
}
