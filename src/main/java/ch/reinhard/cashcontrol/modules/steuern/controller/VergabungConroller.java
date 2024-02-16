package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.VergabungDto;
import ch.reinhard.cashcontrol.modules.steuern.api.VergabungService;
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
@RequestMapping("/api/v1/vergabung")
@Tag(name = "VergabungController", description = "Endpoints für Vergabung")
public class VergabungConroller {
    @Autowired
    private VergabungService vergabungService;

    @Operation(summary = "Alle Vergabungen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Vergabungen gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VergabungDto.class))
                        })
            })
    @GetMapping
    public List<VergabungDto> getAllVergabungen() {
        return vergabungService.getAllVergabung();
    }

    @Operation(summary = "Vergabung finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Vergabung mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = VergabungDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Vergabung nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public VergabungDto getVergabungById(@PathVariable String id) {
        return vergabungService.getVergabungById(id);
    }

    @Operation(summary = "Neue Vergabung erfassen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Vergabung erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createVergabung(@Valid @RequestBody VergabungDto request) {
        var id = vergabungService.createVergabung(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
