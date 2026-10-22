package ch.reinhard.cashcontrol.modules.reporting.adapter.out;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.PersonBo;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/** Rendert den Steuer-Report ohne Spring-Context und DB: Template -> HTML -> PDF. */
class ReportingTemplateTest {

    private HtmlGeneratorAdapter htmlGenerator;
    private final PdfGenerator pdfGenerator = new PdfGenerator();

    @BeforeEach
    void setUp() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/reporting/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        var engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        htmlGenerator = new HtmlGeneratorAdapter(new TemplateRenderer(engine));
    }

    @Test
    void rendertAlleRubrikenMitSummenUndErzeugtEinPdf() {
        var html = htmlGenerator.generateHtml(vollerReport());

        // Thymeleaf escaped den Apostroph (&#39;) -> für die Prüfung zurückwandeln
        assertThat(HtmlUtils.htmlUnescape(html))
                .contains("Steuerrechnung 2025")
                .contains("Reinhard Beat – Muster AG")       // Arbeitnehmer aufgelöst
                .contains("KRANKENKASSE")
                .contains("1'300.00")                        // Kosten-Total 1'000 + 300
                .contains("Fassade streichen")
                .contains("10'000.00")                       // Anteil Unterhalt
                .contains("500'000.00")                      // Hypothek
                .doesNotContain("Keine Einträge");

        assertThat(new String(pdfGenerator.generatePdfFromHtml(html), 0, 5, StandardCharsets.ISO_8859_1))
                .isEqualTo("%PDF-");
    }

    @Test
    void formatiertBetraegeImSchweizerFormat() {
        var variables = PdfVariables.of(vollerReport());

        assertThat(variables.chf(new BigDecimal("1234567.5"))).isEqualTo("1'234'567.50");
        assertThat(variables.chf(new BigDecimal("999.995"))).isEqualTo("1'000.00");
        assertThat(variables.chf(new BigDecimal("-1300"))).isEqualTo("-1'300.00");
        assertThat(variables.chf(BigDecimal.ZERO)).isEqualTo("0.00");
        assertThat(variables.chf(null)).isEmpty();
    }

    @Test
    void zeigtLeereRubrikenAlsKeineEintraege() {
        var leer = new ReportingBo(2025, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                List.of(), List.of(), List.of());

        var html = htmlGenerator.generateHtml(leer);

        assertThat(html.split("Keine Einträge", -1)).hasSize(10); // 9 Rubriken -> 9 Treffer
        assertThat(pdfGenerator.generatePdfFromHtml(html)).isNotEmpty();
    }

    private static ReportingBo vollerReport() {
        var person = new PersonBo("p1", "Reinhard", "Beat", LocalDate.of(1970, 1, 1), "756.1234.5678.97");
        var beruf = new BerufBo("b1", 2025, "p1", "Muster AG", "Bern", 100, 220, 20, "Kein ÖV-Anschluss",
                new BigDecimal("15.00"), null);
        var kosten = List.of(
                new KostenViewBo("k1", null, 2025, KostenArtBo.KRANKENKASSE, "CSS", "p1", new BigDecimal("1000"),
                        null, "Reinhard", "Beat"),
                new KostenViewBo("k2", null, 2025, KostenArtBo.BANKGEBUEHREN, "UBS", "p1", new BigDecimal("300"),
                        "Kontoführung", "Reinhard", "Beat"));
        var vergabung = new VergabungBo("v1", null, 2025, LocalDate.of(2025, 5, 1), "Rotes Kreuz", new BigDecimal("100"));
        var rechnung = new SchuldBo("s1", 2025, SchuldArtBo.RECHNUNG, "Swisscom", null, new BigDecimal("99"), null);
        var hypothek = new SchuldBo("s2", 2025, SchuldArtBo.HYPOTHEK, "UBS", "Festhypothek",
                new BigDecimal("500000"), new BigDecimal("7500"));
        var vermoegenswert = new VermoegenswertBo("w1", 2025, "Auto", 2022, new BigDecimal("32000"));
        var erbschaft = new ErbschaftBo("e1", 2025, ErbschaftArtBo.ERBSCHAFT, new BigDecimal("50000"),
                LocalDate.of(2025, 3, 31), "Hans Muster", "Vater", "Bargeld", null);
        var unterhalt = new GrundstueckunterhaltBo("g1", 2025, LocalDate.of(2025, 4, 15), "Maler AG",
                "Fassade streichen", new BigDecimal("12000"), new BigDecimal("2000"), new BigDecimal("10000"));

        return new ReportingBo(2025, List.of(person), List.of(beruf), kosten, List.of(vergabung), List.of(rechnung),
                List.of(hypothek), List.of(vermoegenswert), List.of(erbschaft), List.of(unterhalt));
    }
}
