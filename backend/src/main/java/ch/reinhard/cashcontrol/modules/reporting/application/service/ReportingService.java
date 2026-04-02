package ch.reinhard.cashcontrol.modules.reporting.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.PersonServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.reporting.application.port.in.ReportingServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.HtmlGeneratorPort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.PdfGeneratorPort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.SchuldServicePort;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.VergabungServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingService implements ReportingServicePort {

    private final PersonServicePort personServicePort;
    private final VergabungServicePort vergabungServicePort;

    private final HtmlGeneratorPort htmlGeneratorPort;
    private final PdfGeneratorPort pdfGeneratorPort;
    private final SchuldServicePort schuldServicePort;

    @Override
    public byte[] generatePdf() {
        var reportingBo = createReportingBo();
        var html = htmlGeneratorPort.generateHtml(reportingBo);
        return pdfGeneratorPort.generatePdf(html);
    }

    private ReportingBo createReportingBo() {

        var personen = personServicePort.getAllPerson();
        var vergabungen = vergabungServicePort.getAllVergabung();
        var rechnungen = schuldServicePort.getRechnungen();
        var hypotheken = schuldServicePort.getHypotheken();
        // TODO evtl. besser eine ReportingBoFactory zu machen

        return new ReportingBo(personen, vergabungen, rechnungen, hypotheken);
    }
}
