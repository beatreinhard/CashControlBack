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
    DIVERSES,
    KRANKENKASSE,
    BERUF,
    BANKGEBUEHREN;

    public static boolean isKategorieForKosten(AusgabeEventKategorie kategorie) {
        if (kategorie == AusgabeEventKategorie.KRANKENKASSE ||
                kategorie == AusgabeEventKategorie.BERUF ||
                kategorie == AusgabeEventKategorie.BANKGEBUEHREN ||
                kategorie == AusgabeEventKategorie.GESUNDHEIT) {
            return true;
        }
        return false;
    }

    public static boolean isKategorieForVergabung(AusgabeEventKategorie kategorie) {
        return kategorie == AusgabeEventKategorie.SPENDEN;
    }
}
