package ch.reinhard.cashcontrol.modules.steuern.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "KostenArt")
public enum KostenArtDto {
    DEPOTGEBUEHREN,
    KRANKENKASSE,
    UNFALL_KRANKHEIT,
    BERUF,
    GRUNDSTUECK_BETRIEB
}
