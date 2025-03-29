package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import io.swagger.v3.oas.annotations.media.Schema;

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
