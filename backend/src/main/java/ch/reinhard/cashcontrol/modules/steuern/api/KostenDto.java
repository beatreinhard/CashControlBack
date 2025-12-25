package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "Kosten")
public record KostenDto(
        String id,
        @NotNull Integer jahr,
        @NotNull KostenArtDto art,
        @NotNull String empfaenger,
        @NotNull String bezahler,
        @NotNull BigDecimal betrag,
        String bemerkung) {}
