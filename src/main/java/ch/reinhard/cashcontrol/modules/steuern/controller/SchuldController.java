package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.SchuldDto;
import ch.reinhard.cashcontrol.modules.steuern.api.SchuldService;
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
@RequestMapping("/api/v1/schuld")
@Tag(name = "SchuldController", description = "Endpoints für Schuld")
public class SchuldController {

    private final SchuldService schuldService;

    @Operation(summary = "Alle Schulden")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Schulden gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SchuldDto.class))
                        })
            })
    @GetMapping
    public List<SchuldDto> getAllSchulden() {
        return schuldService.getAllSchuld();
    }

    @Operation(summary = "Schuld finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Schuld mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = SchuldDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Schuld nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public SchuldDto getSchuldById(@PathVariable String id) {
        return schuldService.getSchuldById(id);
    }

    @Operation(summary = "Neue Schuld erfassen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Schuld erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createSchuld(@Valid @RequestBody SchuldDto request) {
        var id = schuldService.createSchuld(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
