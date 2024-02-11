package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.VergabungDto;
import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertDto;
import ch.reinhard.cashcontrol.modules.steuern.api.VermoegenswertService;
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
@RequestMapping("/api/v1/vermoegenswert")
@Tag(name = "VermoegenswertController", description = "Endpoints für Vermoegenswert")
public class VermoegenswertController {
    @Autowired
    private VermoegenswertService vermoegenswertService;

    @Operation(summary = "Alle Vermoegenswerte")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Vermoegenswerte gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VermoegenswertDto.class))
                        })
            })
    @GetMapping
    public List<VermoegenswertDto> getAllVermoegenswerte() {
        return vermoegenswertService.getAllVermoegenswert();
    }

    @Operation(summary = "Vermoegenswert finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Vermoegenswert mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VergabungDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Vermoegenswert nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public VermoegenswertDto getVergabungById(@PathVariable String id) {
        return vermoegenswertService.getVermoegenswertById(id);
    }

    @Operation(summary = "Neuen Vermoegenswert anlegen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Vermoegenswert erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createVermoegenswert(@Valid @RequestBody VermoegenswertDto request) {
        var id = vermoegenswertService.createVermoegenswert(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
