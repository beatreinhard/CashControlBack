package ch.reinhard.cashcontrol.modules.finanzen.controller;

import ch.reinhard.cashcontrol.modules.finanzen.api.AusgabeKategorieDto;
import ch.reinhard.cashcontrol.modules.finanzen.api.AusgabeService;
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
@RequestMapping("/api/v1/kategorie")
@Tag(name = "KategorieController", description = "Endpoints für Zahlungs-Kategorien")
public class KategorieController {

    @Autowired
    private AusgabeService kategorieService;

    @Operation(summary = "Alle Kategorien")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Alle Kategorien gefunden",
                        content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AusgabeKategorieDto.class))
                        })
            })

    @GetMapping
    public List<AusgabeKategorieDto> getAllKategorie() {
        return kategorieService.getAllKategorie();
    }

    @Operation(summary = "Neue Kategorie anlegen")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Kategorie erfasst",
                        content =
                                @Content(
                                        mediaType = MediaType.TEXT_PLAIN_VALUE,
                                        schema = @Schema(implementation = String.class)))
            })
    @PostMapping
    public ResponseEntity<String> createKategorie(@Valid @RequestBody String bezeichnung) {
        var id = kategorieService.createKategorie(bezeichnung);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
