package ch.reinhard.cashcontrol.modules.steuern.api;

import ch.reinhard.cashcontrol.openapi.model.KostenDto;

import java.util.List;

public interface KostenService {
    String createKosten(KostenDto source);

    KostenDto getKostenById(String id);

    List<KostenDto> getAllKosten();

    List<KostenDto> getKostenByJahr(Integer jahr);

    void updateKosten(KostenDto source);

    void deleteKostenById(String id);
}
