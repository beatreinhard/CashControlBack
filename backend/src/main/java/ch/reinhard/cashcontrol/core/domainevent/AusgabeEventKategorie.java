package ch.reinhard.cashcontrol.core.domainevent;

public enum AusgabeEventKategorie {
    VERKEHR_AUTO_TRANSPORT,
    HAUSHALT,
    FERIEN,
    KOMMUNIKATION_MEDIEN,
    GESUNDHEIT,
    PERSOENLICHE_AUSGABEN,
    SPAREN_VORSORGE,
    STEUERN,
    SPENDEN,
    KRANKENKASSE,
    BERUF,
    BANKGEBUEHREN,
    LIEGENSCHAFT_BETRIEB,
    DIVERSES;

    public static boolean isKategorieForKosten(AusgabeEventKategorie kategorie) {
        return kategorie == AusgabeEventKategorie.KRANKENKASSE ||
                kategorie == AusgabeEventKategorie.BERUF ||
                kategorie == AusgabeEventKategorie.BANKGEBUEHREN ||
                kategorie == AusgabeEventKategorie.GESUNDHEIT ||
                kategorie == AusgabeEventKategorie.LIEGENSCHAFT_BETRIEB;
    }

    public static boolean isKategorieForVergabung(AusgabeEventKategorie kategorie) {
        return kategorie == AusgabeEventKategorie.SPENDEN;
    }
}
