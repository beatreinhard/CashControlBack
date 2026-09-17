package ch.reinhard.cashcontrol.modules.steuern.application.port.out.persistence;

import ch.reinhard.cashcontrol.modules.steuern.application.domain.BerufBo;

import java.util.List;

public interface BerufPersistencePort {
    String createBeruf(BerufBo source);

    BerufBo getBerufById(String id);

    List<BerufBo> getAllBeruf();

    void updateBeruf(BerufBo source);

    void deleteBerufById(String id);
}
