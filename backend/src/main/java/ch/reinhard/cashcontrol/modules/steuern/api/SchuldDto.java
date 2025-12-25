package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "Schuld")
public record SchuldDto(
        String id,
        @NotNull Integer jahr,
        @NotNull SchuldArtDto art,
        @NotNull String glaeubiger,
        @NotNull BigDecimal betrag,
        BigDecimal zinsen) {}
