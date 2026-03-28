package ch.reinhard.cashcontrol.modules.reporting.adapter.in.web;

import ch.reinhard.cashcontrol.modules.reporting.application.port.in.ReportingServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReportingController {
    private final ReportingServicePort reportingServicePort;


    @GetMapping(value = "/api/v1/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> report() {
        byte[] pdf = reportingServicePort.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename("report.pdf")
                        .build()
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }
}
