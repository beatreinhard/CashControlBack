package ch.reinhard.cashcontrol.modules.finanzen.controller;

import ch.reinhard.cashcontrol.modules.finanzen.api.AusgabeDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.AusgabeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/ausgabe")
@Tag(name = "AusgabeController", description = "Endpoints für Ausgaben")
public class AusgabeController {


    private final AusgabeService ausgabeService;

    @Operation(summary = "Alle Ausgaben")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Ausgaben gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AusgabeDto.class))
                        })
            })
    @GetMapping
    public List<AusgabeDto> getAllAusgabe() {
        return ausgabeService.getAllAusgabe();
    }

    @Operation(summary = "Ausgabe finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ausgabe mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AusgabeDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Ausgabe nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public AusgabeDto getAusgabeById(@PathVariable String id) {
        return ausgabeService.getAusgabeById(id);
    }

    @Operation(summary = "Neue Ausgabe erfassen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Ausgabe erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createAusgabe(@Valid @RequestBody AusgabeDto request) {
        var id = ausgabeService.createAusgabe(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

}
