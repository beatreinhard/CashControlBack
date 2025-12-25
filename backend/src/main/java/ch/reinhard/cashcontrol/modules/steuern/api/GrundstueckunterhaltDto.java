package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Grundstueckunterhalt")
public record GrundstueckunterhaltDto(
        String id,
        @NotNull Integer jahr,
        @NotNull LocalDate rgDatum,
        @NotNull String ausfuehrendeFirma,
        @NotNull String arbeitsArt,
        @NotNull BigDecimal betragNetto,
        @NotNull BigDecimal anteilAndereKosten,
        @NotNull BigDecimal anteilUnterhalt) {}
