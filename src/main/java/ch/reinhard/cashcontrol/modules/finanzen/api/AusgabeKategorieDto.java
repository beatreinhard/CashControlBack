package ch.reinhard.cashcontrol.modules.finanzen.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "AusgabeKategorie")
public enum AusgabeKategorieDto {
    VERKEHR_AUTO_TRANSPORT,
    HAUSHALT,
    FERIEN,
    KOMMUNIKATION_MEDIEN,
    GESUNDHEIT,
    PERSOENLICHE_AUSGABEN,
    SPAREN_VORSORGE,
    STEUERN,
    DIVERSES
}
