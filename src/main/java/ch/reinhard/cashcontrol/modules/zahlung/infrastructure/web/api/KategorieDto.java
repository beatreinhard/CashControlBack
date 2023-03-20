package ch.reinhard.cashcontrol.modules.zahlung.infrastructure.web.api;

public record KategorieDto(
    String id,
    Long version,
    String bezeichnung
){}
