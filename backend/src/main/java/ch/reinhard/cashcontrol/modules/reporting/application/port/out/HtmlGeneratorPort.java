package ch.reinhard.cashcontrol.modules.reporting.application.port.out;

import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;

public interface HtmlGeneratorPort {
    String generateHtml(ReportingBo reportingBo);
}
