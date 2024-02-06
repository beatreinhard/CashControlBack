package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(name = "Beruf")
public record BerufDto(
        String id,
        Long version,
        @NotNull Integer jahr,
        @NotNull String arbeitnehmer,
        @NotNull String arbeitgeber,
        @NotNull String arbeitsort,
        @NotNull Integer beschaeftigungsgrad,
        @NotNull Integer arbeitstage,
        Integer fahrtkilometerProTag,
        String grundAutobenutzung,
        @NotNull BigDecimal ansatzAuswaertigeVerpflegung,
        String bemerkung) {}
