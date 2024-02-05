package ch.reinhard.cashcontrol.modules.finanzen.api;

public record ZahlungUpdateDto(
    String id,
    Long version,
    ZahlungDetailsDto details
){}
