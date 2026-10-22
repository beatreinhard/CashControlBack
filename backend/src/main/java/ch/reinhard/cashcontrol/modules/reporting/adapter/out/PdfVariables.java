package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Builder
@Getter
public class PdfVariables {
    private static final DecimalFormatSymbols CHF_SYMBOLS = new DecimalFormatSymbols();

    static {
        CHF_SYMBOLS.setGroupingSeparator('\'');
        CHF_SYMBOLS.setDecimalSeparator('.');
    }

    private Integer jahr;
    private List<PersonBo> personen;
    private List<BerufBo> berufe;
    private List<KostenViewBo> kosten;
    private List<VergabungBo> vergabungen;
    private List<SchuldBo> rechnungen;
    private List<SchuldBo> hypotheken;
    private List<VermoegenswertBo> vermoegenswerte;
    private List<ErbschaftBo> erbschaften;
    private List<GrundstueckunterhaltBo> grundstueckunterhalte;

    public static PdfVariables of(ReportingBo reportingBo) {
        return PdfVariables.builder().
                jahr(reportingBo.jahr()).
                personen(reportingBo.personen()).
                berufe(reportingBo.berufe()).
                kosten(reportingBo.kosten().stream()
                        .sorted(Comparator.comparing(KostenViewBo::getArt).thenComparing(KostenViewBo::getEmpfaenger))
                        .toList()).
                vergabungen(reportingBo.vergabungen().stream()
                        .sorted(Comparator.comparing(VergabungBo::getZahlungsDatum))
                        .toList()).
                rechnungen(reportingBo.rechnungen()).
                hypotheken(reportingBo.hypotheken()).
                vermoegenswerte(reportingBo.vermoegenswerte()).
                erbschaften(reportingBo.erbschaften().stream()
                        .sorted(Comparator.comparing(ErbschaftBo::getDatum))
                        .toList()).
                grundstueckunterhalte(reportingBo.grundstueckunterhalte().stream()
                        .sorted(Comparator.comparing(GrundstueckunterhaltBo::getRgDatum))
                        .toList()).
                build();
    }

    /** Betrag im Schweizer Format mit zwei Nachkommastellen, z.B. 1'300.00; leer wenn kein Betrag. */
    public String chf(BigDecimal betrag) {
        if (betrag == null) return "";
        // DecimalFormat ist nicht thread-safe -> pro Aufruf neu erzeugen
        var format = new DecimalFormat("#,##0.00", CHF_SYMBOLS);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(betrag);
    }

    /** "Name Vorname" der Person mit der ID, leer wenn unbekannt (z.B. Arbeitnehmer eines Berufs). */
    public String personName(String personId) {
        return personen.stream()
                .filter(p -> Objects.equals(p.getId(), personId))
                .map(p -> p.getName() + " " + p.getVorname())
                .findFirst()
                .orElse("");
    }

    // -----------------------
    // Summen für die Tabellen
    // -----------------------

    public BigDecimal getKostenTotal() {
        return sum(kosten, KostenViewBo::getBetrag);
    }

    public BigDecimal getVergabungenTotal() {
        return sum(vergabungen, VergabungBo::getBetrag);
    }

    public BigDecimal getRechnungenTotal() {
        return sum(rechnungen, SchuldBo::getBetrag);
    }

    public BigDecimal getHypothekenTotal() {
        return sum(hypotheken, SchuldBo::getBetrag);
    }

    public BigDecimal getHypothekenZinsenTotal() {
        return sum(hypotheken, SchuldBo::getZinsen);
    }

    public BigDecimal getVermoegenswerteTotal() {
        return sum(vermoegenswerte, VermoegenswertBo::getAnschaffungspreis);
    }

    public BigDecimal getErbschaftenTotal() {
        return sum(erbschaften, ErbschaftBo::getBetrag);
    }

    public BigDecimal getGrundstueckunterhaltNettoTotal() {
        return sum(grundstueckunterhalte, GrundstueckunterhaltBo::getBetragNetto);
    }

    public BigDecimal getGrundstueckunterhaltAndereKostenTotal() {
        return sum(grundstueckunterhalte, GrundstueckunterhaltBo::getAnteilAndereKosten);
    }

    public BigDecimal getGrundstueckunterhaltUnterhaltTotal() {
        return sum(grundstueckunterhalte, GrundstueckunterhaltBo::getAnteilUnterhalt);
    }

    private static <T> BigDecimal sum(List<T> items, Function<T, BigDecimal> value) {
        return items.stream().map(value).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
