package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface KostenService {
    String createKosten(KostenDto source);

    KostenDto getKostenById(String id);

    List<KostenDto> getAllKosten();

    void updateKosten(KostenDto source);

    void deleteKostenById(String id);
}
