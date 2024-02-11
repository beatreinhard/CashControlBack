package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltDto;
import ch.reinhard.cashcontrol.modules.steuern.api.GrundstueckunterhaltService;
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
@RequestMapping("/api/v1/grundstueckunterhalt")
@Tag(name = "GrundstueckunterhaltController", description = "Endpoints für Grundstueckunterhalt")
public class GrundstueckunterhaltController {
    @Autowired
    private GrundstueckunterhaltService grundstueckunterhaltService;

    @Operation(summary = "Alle Grundstueckunterhalte")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Grundstueckunterhalte gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GrundstueckunterhaltDto.class))
                        })
            })
    @GetMapping
    public List<GrundstueckunterhaltDto> getAllGrundstueckunterhalt() {
        return grundstueckunterhaltService.getAllGrundstueckunterhalt();
    }

    @Operation(summary = "Grundstueckunterhalt finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Grundstueckunterhalt mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = GrundstueckunterhaltDto.class))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Grundstueckunterhalt nicht gefunden",
                        content = @Content)
            })
    @GetMapping("/{id}")
    public GrundstueckunterhaltDto getGrundstueckunterhaltById(@PathVariable String id) {
        return grundstueckunterhaltService.getGrundstueckunterhaltById(id);
    }

    @Operation(summary = "Neue Grundstueckunterhalt anlegen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Grundstueckunterhalt erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createGrundstueckunterhalt(@Valid @RequestBody GrundstueckunterhaltDto request) {
        var id = grundstueckunterhaltService.createGrundstueckunterhalt(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
