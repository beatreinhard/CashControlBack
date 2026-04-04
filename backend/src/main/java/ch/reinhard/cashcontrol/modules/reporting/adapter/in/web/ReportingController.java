package ch.reinhard.cashcontrol.modules.reporting.adapter.in.web;

import ch.reinhard.cashcontrol.modules.reporting.application.port.in.ReportingServicePort;
import ch.reinhard.cashcontrol.openapi.api.ReportingControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReportingController implements ReportingControllerApi {
    private final ReportingServicePort reportingServicePort;


    @Override
    public ResponseEntity<Resource> generateReport() {
        byte[] pdf = reportingServicePort.generatePdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.inline()
                        .filename("report.pdf")
                        .build()
        );

        Resource resource = new ByteArrayResource(pdf);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);
    }
}
