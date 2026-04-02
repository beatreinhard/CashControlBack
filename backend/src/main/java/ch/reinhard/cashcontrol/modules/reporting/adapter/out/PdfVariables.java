package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.VergabungBo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PdfVariables {
    private List<PersonBo> persons;
    private List<VergabungBo> vergabungen;
    private List<SchuldBo> rechnungen;
    private List<SchuldBo> hypotheken;

    public static PdfVariables of(ReportingBo reportingBo) {
        return PdfVariables.builder().
                persons(reportingBo.persons()).
                vergabungen(reportingBo.vergabungen()).
                rechnungen(reportingBo.rechnungen()).
                hypotheken(reportingBo.hypotheken()).
                build();
    }
}
