package ch.reinhard.cashcontrol.modules.reporting.application.port.out;

public interface PdfGeneratorPort {
    byte[] generatePdf(String html);
}
