package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.controller;

import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api.ZahlungDto;
import ch.reinhard.cashcontrol.modules.zahlung.service.ZahlungService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "ZahlungController", description = "Endpoints für Zahlungen")
public class ZahlungController {

    @Autowired
    private ZahlungService zahlungService;

    @Operation(summary = "Alle Zahlungen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alle Zahlungen gefunden",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ZahlungDto.class)) })
    })
    @GetMapping("/zahlung")
    public List<ZahlungDto> getAllZahlung() {
        return zahlungService.getAllZahlung();
    }

    @Operation(summary = "Zahlung finden mit ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zahlung gefunden",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ZahlungDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Zahlung nicht gefunden",
                    content = @Content)
    })
    @GetMapping("/zahlung/{id}")
    public ZahlungDto getZahlungById(@PathVariable String id) {
        return zahlungService.getZahlungById(id);
    }


    @Operation(summary = "Neue Zahlung anlegen")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zahlung erfasst",
                    content = @Content(mediaType = "text/plain;charset=UTF-8", schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<String> createZahlung(@Valid @RequestBody ZahlungDetailsDto request) {
        var id = zahlungService.createZahlung(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
