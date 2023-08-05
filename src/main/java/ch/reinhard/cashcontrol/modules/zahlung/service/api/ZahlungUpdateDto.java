package ch.reinhard.cashcontrol.modules.zahlung.service.api;

public record ZahlungUpdateDto(
    String id,
    Long version,
    ZahlungDetailsDto details
){}
