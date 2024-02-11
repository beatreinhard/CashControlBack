package ch.reinhard.cashcontrol.modules.finanzen.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "ZahlungDetails")
public record ZahlungDetailsDto(
        @NotNull LocalDate datum,
        @NotNull String empfaenger,
        @NotNull String kategorieId,
        @NotNull String text,
        @NotNull BigDecimal betrag
){}
