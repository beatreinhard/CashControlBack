package ch.reinhard.cashcontrol.modules.finanzen.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Zahlung")
public record ZahlungDto(
    String id,
    Long version,
    @NotNull LocalDate datum,
    @NotNull String empfaenger,
    @NotNull String kategorieId,
    @NotNull String text,
    @NotNull BigDecimal betrag
){}
