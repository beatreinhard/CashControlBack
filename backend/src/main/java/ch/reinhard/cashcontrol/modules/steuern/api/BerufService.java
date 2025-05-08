package ch.reinhard.cashcontrol.modules.steuern.api;

import java.util.List;

public interface BerufService {
    String createBeruf(BerufDto source);

    BerufDto getBerufById(String id);

    List<BerufDto> getAllBeruf();

    void updateBeruf(BerufDto source);

    void deleteBerufById(String id);
}
