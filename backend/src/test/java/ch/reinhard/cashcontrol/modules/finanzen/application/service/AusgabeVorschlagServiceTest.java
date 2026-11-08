package ch.reinhard.cashcontrol.modules.finanzen.application.service;

import ch.reinhard.cashcontrol.modules.finanzen.application.domain.AusgabeVorschlagBo;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport.AusgabeVorschlagQuellePort;
import ch.reinhard.cashcontrol.modules.finanzen.application.port.out.fileimport.ImportDokument;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AusgabeVorschlagServiceTest {

    private static ImportDokument dokument(String dateiname) {
        return new ImportDokument(dateiname, "hash-" + dateiname, new AusgabeVorschlagBo().setDateiname(dateiname));
    }

    @Test
    void gespeicherteDokumente_werdenAlsVerarbeitetMarkiert() {
        var quelle = new FakeQuelle(List.of(dokument("a.json"), dokument("b.json")));
        var service = new AusgabeVorschlagService(quelle, new AusgabeVorschlagDateiImport());

        service.importAusgabeVorschlag();

        assertThat(quelle.verarbeitet).containsExactly("a.json", "b.json");
    }

    @Test
    void fehlerBeimSpeichern_dateiBleibtLiegenUndAndereLaufenWeiter() {
        var quelle = new FakeQuelle(List.of(dokument("kaputt.json"), dokument("ok.json")));
        var dateiImport = new AusgabeVorschlagDateiImport() {
            @Override
            public boolean importiere(ImportDokument dokument) {
                if (dokument.dateiname().equals("kaputt.json")) {
                    throw new IllegalStateException("DB nicht erreichbar");
                }
                return true;
            }
        };
        var service = new AusgabeVorschlagService(quelle, dateiImport);

        service.importAusgabeVorschlag();

        assertThat(quelle.verarbeitet).containsExactly("ok.json");
    }

    @Test
    void zweiterImportWaehrendLaufendemImport_wirftImportLaeuftBereitsException() {
        var quelle = new FakeQuelle(List.of(dokument("a.json")));
        var service = new AusgabeVorschlagService(quelle, new AusgabeVorschlagDateiImport());
        List<Throwable> fehler = new ArrayList<>();
        quelle.beimLesen = q -> {
            try {
                service.importAusgabeVorschlag();
            } catch (ImportLaeuftBereitsException e) {
                fehler.add(e);
            }
        };

        service.importAusgabeVorschlag();

        assertThat(fehler).hasSize(1);
    }

    @Test
    void nachAbgeschlossenemImport_istNeuerImportMoeglich() {
        var quelle = new FakeQuelle(List.of());
        var service = new AusgabeVorschlagService(quelle, new AusgabeVorschlagDateiImport());

        service.importAusgabeVorschlag();
        service.importAusgabeVorschlag();
    }

    @Test
    void nachFehlerInDerQuelle_wirdDieSperreFreigegeben() {
        var quelle = new FakeQuelle(List.of());
        quelle.beimLesen = q -> {
            throw new IllegalStateException("Quelle kaputt");
        };
        var service = new AusgabeVorschlagService(quelle, new AusgabeVorschlagDateiImport());

        assertThatThrownBy(service::importAusgabeVorschlag).isInstanceOf(IllegalStateException.class);
        quelle.beimLesen = q -> { };
        service.importAusgabeVorschlag();
    }

    private static final class FakeQuelle implements AusgabeVorschlagQuellePort {
        private final List<ImportDokument> dokumente;
        private final List<String> verarbeitet = new ArrayList<>();
        private Consumer<FakeQuelle> beimLesen = q -> { };

        FakeQuelle(List<ImportDokument> dokumente) {
            this.dokumente = dokumente;
        }

        @Override
        public List<ImportDokument> findeNeueDokumente() {
            beimLesen.accept(this);
            return dokumente;
        }

        @Override
        public void markiereVerarbeitet(ImportDokument dokument) {
            verarbeitet.add(dokument.dateiname());
        }
    }
}
