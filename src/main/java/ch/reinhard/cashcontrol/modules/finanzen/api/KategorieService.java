package ch.reinhard.cashcontrol.modules.finanzen.api;

import java.util.List;

public interface KategorieService {
    String createKategorie(String bezeichnung);
    KategorieDto getKategorieById(String id);
    List<KategorieDto> getAllKategorie();
    void updateKategorie(KategorieDto kategorieDto);
    void deleteKategorieById(String id);

}
