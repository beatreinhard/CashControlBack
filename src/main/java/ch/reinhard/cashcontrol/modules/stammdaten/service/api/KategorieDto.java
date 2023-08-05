package ch.reinhard.cashcontrol.modules.stammdaten.service.api;

public record KategorieDto(
    String id,
    Long version,
    String bezeichnung
){}
