package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.BerufDto;
import ch.reinhard.cashcontrol.modules.steuern.api.BerufService;
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
@RequestMapping("/api/v1/beruf")
@Tag(name = "BerufController", description = "Endpoints für Beruf")
public class BerufController {


    private final BerufService berufService;

    @Operation(summary = "Alle Berufe")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Berufe gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BerufDto.class))
                        })
            })
    @GetMapping
    public List<BerufDto> getAllBeruf() {
        return berufService.getAllBeruf();
    }

    @Operation(summary = "Beruf finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Beruf mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BerufDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Beruf nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public BerufDto getBerufById(@PathVariable String id) {
        return berufService.getBerufById(id);
    }

    @Operation(summary = "Neuen Beruf erfassen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Beruf erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createBeruf(@Valid @RequestBody BerufDto request) {
        var id = berufService.createBeruf(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
