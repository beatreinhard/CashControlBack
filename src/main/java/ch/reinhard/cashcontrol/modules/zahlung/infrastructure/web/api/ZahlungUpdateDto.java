package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api;

public record ZahlungUpdateDto(
    String id,
    Long version,
    ZahlungDetailsDto details
){}
