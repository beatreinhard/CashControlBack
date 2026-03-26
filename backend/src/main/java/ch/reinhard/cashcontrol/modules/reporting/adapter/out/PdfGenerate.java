package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.reporting.application.port.out.PdfGeneratePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfGenerate implements PdfGeneratePort {
    private final PdfGenerator pdfGenerator;
    private final ThymeleafTemplate templateEngine;

    @Override
    public byte[] generatePdf() {
        log.info("Generating PDF");
        String html = templateEngine.parseThymeleafTemplate();
        return pdfGenerator.generatePdfFromHtml(html);
    }
}
