package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.application.service.AusgabeVorschlagService;
import ch.reinhard.cashcontrol.openapi.api.AusgabeVorschlagControllerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AusgabeVorschlagController implements AusgabeVorschlagControllerApi {

    private final AusgabeVorschlagService service;

    @Override
    public ResponseEntity<Void> importAusgabeVorschlag() {
        // Läuft bereits ein Import, wirft der Service eine ImportLaeuftBereitsException → 409 (DefaultExceptionHandler)
        service.importAusgabeVorschlag();
        return ResponseEntity.noContent().build();
    }
}
