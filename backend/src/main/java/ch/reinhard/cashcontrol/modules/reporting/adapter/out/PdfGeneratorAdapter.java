package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.reporting.application.port.out.PdfGeneratorPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfGeneratorAdapter implements PdfGeneratorPort {
    private final PdfGenerator pdfGenerator;

    @Override
    public byte[] generatePdf(String html) {
        log.debug("Generating PDF");
        return pdfGenerator.generatePdfFromHtml(html);
    }
}
