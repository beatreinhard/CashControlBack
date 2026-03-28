package ch.reinhard.cashcontrol.modules.reporting.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.PersonServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.reporting.application.port.in.ReportingServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.HtmlGeneratorPort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.PdfGeneratorPort;
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

    @Override
    public byte[] generatePdf() {
        var reportingBo = createReportingBo();
        var html = htmlGeneratorPort.generateHtml(reportingBo);
        return pdfGeneratorPort.generatePdf(html);
    }

    private ReportingBo createReportingBo() {

        var persons = personServicePort.getAllPerson();
        var vergabungen = vergabungServicePort.getAllVergabung();
        // TODO evtl. besser eine ReportingBoFactory zu machen

        PersonBo personBo1 = new PersonBo();
        personBo1.setName("Reinhard");
        personBo1.setVorname("Beat");
        personBo1.setAhvnummer("111.222.333.44");
        PersonBo personBo2 = new PersonBo();
        personBo2.setName("Reinhard");
        personBo2.setVorname("Denise");
        personBo2.setAhvnummer("333.666.111.99");
        PersonBo personBo3 = new PersonBo();
        personBo3.setName("Reinhard");
        personBo3.setVorname("Luc");
        personBo3.setAhvnummer("noch keine");

        return new ReportingBo(persons, vergabungen);
    }
}
