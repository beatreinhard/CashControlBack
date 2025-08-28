package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.AusgabeServicePort;
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

import static ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web.AusgabeWebMapper.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/ausgabe")
@Tag(name = "AusgabeController", description = "Endpoints für Ausgaben")
public class AusgabeController {


    private final AusgabeServicePort ausgabeService;

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
        return toAusgabeDtoList(ausgabeService.getAllAusgabe());
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
    public AusgabeDto getAusgabeById(@PathVariable("id") String id) {
        return toAusgabeDto(ausgabeService.getAusgabeById(id));
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
        var id = ausgabeService.createAusgabe(toAusgabeBo(request));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "Ausgabe löschen mit ID")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ausgabe mit ID gelöscht"),
                    @ApiResponse(responseCode = "404", description = "Ausgabe nicht gefunden", content = @Content)
            })
    @DeleteMapping("/{id}")
    public void deleteAusgabeById(@PathVariable("id") String id) {
         ausgabeService.deleteAusgabeById(id);
    }

    @Operation(summary = "Ausgabe aktualisieren")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ausgabe aktualisiert",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = AusgabeDto.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Ausgabe nicht gefunden", content = @Content)
            })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAusgabe(@PathVariable("id") String id, @Valid @RequestBody AusgabeDto request) {
        AusgabeBo ausgabeBo = toAusgabeBo(request);
        ausgabeBo.id(id);
        ausgabeService.updateAusgabe(ausgabeBo);
        return ResponseEntity.ok().build();
    }


}
