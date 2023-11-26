package ch.reinhard.cashcontrol.modules.shared.stammdaten.service.api;

public record KategorieDto(
    String id,
    Long version,
    String bezeichnung
){}
