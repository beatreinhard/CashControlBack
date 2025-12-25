package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Erbschaft")
public record ErbschaftDto(
        String id,
        @NotNull Integer jahr,
        @NotNull ErbschaftArtDto art,
        @NotNull BigDecimal betrag,
        @NotNull LocalDate datum,
        @NotNull String geber,
        @NotNull String verwandtschaftsverhaeltnis,
        @NotNull String gegenstand,
        String bemerkung) {}
