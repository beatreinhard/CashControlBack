package ch.reinhard.cashcontrol.modules.reporting.application.service;

import ch.reinhard.cashcontrol.modules.reporting.application.port.in.ReportingServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.PdfGeneratePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingService implements ReportingServicePort {

    private final PdfGeneratePort pdfGeneratePort;

    @Override
    public byte[] getReportingPdf() {
        return pdfGeneratePort.generatePdf();
    }
}
