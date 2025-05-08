package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "Vermoegenswert")
public record VermoegenswertDto(
        String id,
        Long version,
        @NotNull Integer jahr,
        @NotNull String bezeichnung,
        @NotNull Integer anschaffungsjahr,
        @NotNull BigDecimal anschaffungspreis) {}
