package ch.reinhard.cashcontrol.modules.reporting.application.domain;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.*;

import java.util.List;

public record ReportingBo(
        Integer jahr,
        List<PersonBo> personen,
        List<BerufBo> berufe,
        List<KostenViewBo> kosten,
        List<VergabungBo> vergabungen,
        List<SchuldBo> rechnungen,
        List<SchuldBo> hypotheken,
        List<VermoegenswertBo> vermoegenswerte,
        List<ErbschaftBo> erbschaften,
        List<GrundstueckunterhaltBo> grundstueckunterhalte
) {}
