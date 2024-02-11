package ch.reinhard.cashcontrol.modules.finanzen.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Kategorie")
public record KategorieDto(String id, Long version,  @NotNull String bezeichnung) {}
