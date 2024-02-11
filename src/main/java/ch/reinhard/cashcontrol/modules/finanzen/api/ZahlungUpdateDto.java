package ch.reinhard.cashcontrol.modules.finanzen.api;

import jakarta.validation.constraints.NotNull;

public record ZahlungUpdateDto(
    String id,
    Long version,
    @NotNull ZahlungDetailsDto details
){}
