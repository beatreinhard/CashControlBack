package ch.reinhard.cashcontrol.modules.reporting.application.domain;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;

import java.util.List;

public record ReportingBo(
        List<PersonBo> personen,
        List<VergabungBo> vergabungen,
        List<SchuldBo> rechnungen,
        List<SchuldBo> hypotheken
) {}
