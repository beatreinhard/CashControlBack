package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

// TODO: remove when old controller is deleted and openapi is fully implemented
@Schema(name = "Zahlung")
public record AusgabeDto(
    String id,
    Long version,
    @NotNull LocalDate datum,
    @NotNull String empfaenger,
    @NotNull AusgabeKategorieDto kategorie,
    @NotNull String text,
    @NotNull BigDecimal betrag
){}
