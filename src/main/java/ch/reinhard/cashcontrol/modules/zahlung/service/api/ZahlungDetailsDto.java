package ch.reinhard.cashcontrol.modules.zahlung.service.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(name = "ZahlungDetails")
public record ZahlungDetailsDto(
    LocalDate datum,
    String empfaenger,
    String kategorieId,
    String text,
    Long betrag
){}
