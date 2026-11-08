package ch.reinhard.cashcontrol.modules.finanzen.adapter.out.fileimport;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeKategorieBo;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Format einer Ausgabe in den Import-Dateien.
 */
record AusgabeVorschlagJsonDto(
        LocalDate datum,
        String empfaenger,
        AusgabeKategorieBo kategorie,
        String text,
        BigDecimal betrag) {
}
