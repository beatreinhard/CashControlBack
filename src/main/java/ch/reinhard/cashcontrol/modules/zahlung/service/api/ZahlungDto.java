package ch.reinhard.cashcontrol.modules.zahlung.service.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Zahlung")
public record ZahlungDto(
    String id,
    Long version,
    LocalDate datum,
    String empfaenger,
    String kategorieId,
    String text,
    BigDecimal betrag
){}
