package ch.reinhard.cashcontrol.modules.reporting.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.port.in.PersonServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.domain.ReportingBo;
import ch.reinhard.cashcontrol.modules.reporting.application.port.in.ReportingServicePort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.HtmlGeneratorPort;
import ch.reinhard.cashcontrol.modules.reporting.application.port.out.PdfGeneratorPort;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldArtBo;
import ch.reinhard.cashcontrol.modules.steuern.application.domain.SchuldBo;
import ch.reinhard.cashcontrol.modules.steuern.application.port.in.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReportingService implements ReportingServicePort {

    private final PersonServicePort personServicePort;
    private final BerufServicePort berufServicePort;
    private final KostenServicePort kostenServicePort;
    private final VergabungServicePort vergabungServicePort;
    private final SchuldServicePort schuldServicePort;
    private final VermoegenswertServicePort vermoegenswertServicePort;
    private final ErbschaftServicePort erbschaftServicePort;
    private final GrundstueckunterhaltServicePort grundstueckunterhaltServicePort;

    private final HtmlGeneratorPort htmlGeneratorPort;
    private final PdfGeneratorPort pdfGeneratorPort;

    @Override
    public byte[] generatePdf(Integer jahr) {
        var reportingBo = createReportingBo(jahr);
        var html = htmlGeneratorPort.generateHtml(reportingBo);
        return pdfGeneratorPort.generatePdf(html);
    }

    private ReportingBo createReportingBo(Integer jahr) {
        var schulden = schuldServicePort.getSchuldByJahr(jahr);
        // TODO evtl. besser eine ReportingBoFactory zu machen

        return new ReportingBo(
                jahr,
                personServicePort.getAllPerson(),
                byJahr(berufServicePort.getAllBeruf(), b -> b.getJahr(), jahr),
                kostenServicePort.getKostenViewByJahr(jahr),
                vergabungServicePort.getVergabungByJahr(jahr),
                byArt(schulden, SchuldArtBo.RECHNUNG),
                byArt(schulden, SchuldArtBo.HYPOTHEK),
                byJahr(vermoegenswertServicePort.getAllVermoegenswert(), v -> v.getJahr(), jahr),
                byJahr(erbschaftServicePort.getAllErbschaft(), e -> e.getJahr(), jahr),
                byJahr(grundstueckunterhaltServicePort.getAllGrundstueckunterhalt(), g -> g.getJahr(), jahr)
        );
    }

    /** Für Domains ohne Jahres-Abfrage im Port wird hier gefiltert. */
    private static <T> List<T> byJahr(List<T> items, Function<T, Integer> jahrOf, Integer jahr) {
        return items.stream().filter(item -> Objects.equals(jahrOf.apply(item), jahr)).toList();
    }

    private static List<SchuldBo> byArt(List<SchuldBo> schulden, SchuldArtBo art) {
        return schulden.stream()
                .filter(s -> s.getArt() == art)
                .sorted(Comparator.comparing(SchuldBo::getGlaeubiger, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}
