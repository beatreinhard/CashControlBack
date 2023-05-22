package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "Zahlung")
public record ZahlungDto(
    String id,
    Long version,
    LocalDate datum,
    String empfaenger,
    String kategorieId,
    String text,
    Long betrag
){}
