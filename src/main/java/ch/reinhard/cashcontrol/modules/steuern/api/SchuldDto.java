package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "Schuld")
public record SchuldDto(
        String id,
        Long version,
        @NotNull Integer jahr,
        @NotNull String glaeubiger,
        @NotNull SchuldArtDto art,
        @NotNull BigDecimal betrag,
        BigDecimal zinsen) {}
