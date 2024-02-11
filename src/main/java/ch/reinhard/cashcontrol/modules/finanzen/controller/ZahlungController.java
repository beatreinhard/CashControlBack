package ch.reinhard.cashcontrol.modules.finanzen.controller;

import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungDetailsDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungService;
import ch.reinhard.cashcontrol.modules.finanzen.api.ZahlungViewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zahlung")
@Tag(name = "ZahlungController", description = "Endpoints für Zahlungen")
public class ZahlungController {

    @Autowired
    private ZahlungService zahlungService;

    @Operation(summary = "Alle Zahlungen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Zahlungen gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ZahlungDto.class))
                        })
            })
    @GetMapping
    public List<ZahlungDto> getAllZahlung() {
        return zahlungService.getAllZahlung();
    }

    @Operation(summary = "Zahlung finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Zahlung mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ZahlungDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Zahlung nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public ZahlungDto getZahlungById(@PathVariable String id) {
        return zahlungService.getZahlungById(id);
    }

    @Operation(summary = "Neue Zahlung anlegen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Zahlung erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createZahlung(@Valid @RequestBody ZahlungDetailsDto request) {
        var id = zahlungService.createZahlung(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "Zahlungen suchen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Zahlungen gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ZahlungViewDto.class))
                        })
            })
    @GetMapping("/search")
    public List<ZahlungViewDto> searchZahlungen() {

        // TODO Dto als Suchfilter übergeben
        var empfaenger = "Spotify";

        return zahlungService.searchZahlungen(empfaenger);
    }
}
