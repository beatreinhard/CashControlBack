package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltDto;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenDto;
import ch.reinhard.cashcontrol.modules.steuern.api.KostenService;
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
@RequestMapping("/api/v1/kosten")
@Tag(name = "KostenController", description = "Endpoints für Kosten")
public class KostenController {

    private final KostenService kostenService;

    @Operation(summary = "Alle Kosten")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Kosten gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GrundstueckunterhaltDto.class))
                        })
            })
    @GetMapping
    public List<KostenDto> getAllKosten() {
        return kostenService.getAllKosten();
    }

    @Operation(summary = "Kosten finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Kosten mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = KostenDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Kosten nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public KostenDto getKostenById(@PathVariable String id) {
        return kostenService.getKostenById(id);
    }

    @Operation(summary = "Neue Kosten erfassen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Kosten erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createKosten(@Valid @RequestBody KostenDto request) {
        var id = kostenService.createKosten(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
