package ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;

import java.util.List;

public interface VergabungPersistencePort {
    String createVergabung(VergabungBo source);
    VergabungBo getVergabungById(String id);
    void updateVergabung(VergabungBo source);
    void deleteVergabungById(String id);
    List<VergabungBo> getAllVergabung();
}
