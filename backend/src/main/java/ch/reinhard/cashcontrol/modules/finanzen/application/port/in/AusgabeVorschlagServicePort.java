package ch.reinhard.cashcontrol.modules.finanzen.application.port.in;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagBo;

import java.util.List;

public interface AusgabeVorschlagServicePort {
    List<AusgabeVorschlagBo> getAllAusgabeVorschlag();
    void importAusgabeVorschlag();
}
