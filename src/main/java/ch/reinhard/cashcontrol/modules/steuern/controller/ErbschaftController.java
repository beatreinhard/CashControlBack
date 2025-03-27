package ch.reinhard.cashcontrol.modules.steuern.controller;

import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftDto;
import ch.reinhard.cashcontrol.modules.steuern.api.ErbschaftService;
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
@RequestMapping("/api/v1/erbschaft")
@Tag(name = "ErbschaftController", description = "Endpoints für Erbschaft")
public class ErbschaftController {

    private final ErbschaftService erbschaftService;

    @Operation(summary = "Alle Erbschaften")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Erbschaften gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErbschaftDto.class))
                        })
            })
    @GetMapping
    public List<ErbschaftDto> getAllBeruf() {
        return erbschaftService.getAllErbschaft();
    }

    @Operation(summary = "Erbschaft finden mit ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Erbschaft mit ID gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErbschaftDto.class))
                        }),
                @ApiResponse(responseCode = "404", description = "Erbschaft nicht gefunden", content = @Content)
            })
    @GetMapping("/{id}")
    public ErbschaftDto getErbschaftById(@PathVariable String id) {
        return erbschaftService.getErbschaftById(id);
    }

    @Operation(summary = "Neue Erbschaft erfassen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Erbschaft erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createErbschaft(@Valid @RequestBody ErbschaftDto request) {
        var id = erbschaftService.createErbschaft(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
