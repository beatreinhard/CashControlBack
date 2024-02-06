package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Vergabung")
public record VergabungDto(
        String id,
        Long version,
        @NotNull Integer jahr,
        @NotNull LocalDate zahlungsDatum,
        @NotNull String empfaenger,
        @NotNull BigDecimal betrag) {}
