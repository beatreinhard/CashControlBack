package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.HtmlGeneratorPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class HtmlGeneratorAdapter implements HtmlGeneratorPort {
    private final TemplateRenderer templateRenderer;

    @Override
    public String generateHtml(ReportingBo reportingBo) {
        var context = createContext(reportingBo);
        log.debug("Generating HTML");
        return templateRenderer.render(context);
    }

    private static Context createContext(ReportingBo reportingBo) {
        var pdfVariables = PdfVariables.of(reportingBo);
        Context context = new Context();
        context.setVariable("var", pdfVariables);
        return context;
    }
}
