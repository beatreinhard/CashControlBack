# Designvorschlag: JSON-Import als Ausgabe-Vorschläge mit Freigabe

Ein Scheduler liest JSON-Dateien von einem Host-Laufwerk und speichert sie als Vorschläge in einer separaten Tabelle. Der Benutzer prüft die Vorschläge in einem eigenen UI und genehmigt sie für die definitive Verbuchung in `ausgabe`.

Der Vorschlag hält sich an die bestehende Struktur in `modules/finanzen` (`application/domain|service|port`, `adapter/in|out`). Er passt zur jetzigen Ausgabe-Logik und zum Betrieb in Docker.

## 1. Überblick

```
                 ┌──────────────── adapter/in ───────────────┐
                 │                                           │
  @Scheduled ──► │ AusgabeVorschlagImportScheduler           │
  (cron)         │        │                                  │
                 │        ▼                                  │
  Angular UI ──► │ AusgabeVorschlagController (REST)         │
                 └────────┬──────────────────────────────────┘
                          │  port/in
                          ▼
        ┌─────────────── application ────────────────────────┐
        │ AusgabeVorschlagImportService                      │
        │ AusgabeVorschlagService ──► AusgabeServicePort ────┼──► bestehende Verbuchung
        │ AusgabeVorschlagBo (Status, Regeln)                │    (inkl. AusgabeCreatedEvent
        └────────┬───────────────────────────┬───────────────┘     → steuern-Consumer)
                 │ port/out                  │ port/out
                 ▼                           ▼
   AusgabeVorschlagQuellePort      AusgabeVorschlagPersistencePort
                 │                           │
   ┌─────────────▼─────────┐     ┌───────────▼──────────────┐
   │ JsonDateiQuelleAdapter│     │ AusgabeVorschlag-        │
   │ (Filesystem, Jackson) │     │ PersistenceAdapter (JPA) │
   └─────────────┬─────────┘     └───────────┬──────────────┘
                 ▼                           ▼
   Host-Laufwerk (Bind-Mount)      Tabelle ausgabe_vorschlag
```

Grundidee: Der Scheduler ist **ein treibender Adapter wie der REST-Controller**. Er ruft nur einen In-Port auf und enthält selbst keine Logik. Das Dateisystem ist **ein getriebener Adapter wie die Datenbank**. Der Service weiss nicht, dass die Daten aus JSON-Dateien kommen.

## 2. Pakete und Klassen

```
modules/finanzen/
├── application/
│   ├── domain/
│   │   ├── AusgabeVorschlagBo.java          // Daten + Status + Regeln
│   │   └── AusgabeVorschlagStatusBo.java    // OFFEN, GENEHMIGT, VERWORFEN
│   ├── port/in/
│   │   ├── AusgabeVorschlagImportPort.java  // ImportResultat importieren()
│   │   └── AusgabeVorschlagServicePort.java // lesen, korrigieren, genehmigen, verwerfen
│   ├── port/out/
│   │   ├── quelle/AusgabeVorschlagQuellePort.java
│   │   └── persistence/AusgabeVorschlagPersistencePort.java
│   └── service/
│       ├── AusgabeVorschlagImportService.java
│       └── AusgabeVorschlagService.java
└── adapter/
    ├── in/scheduler/AusgabeVorschlagImportScheduler.java
    ├── in/web/AusgabeVorschlagController.java + AusgabeVorschlagWebMapper.java
    ├── out/quelle/JsonDateiQuelleAdapter.java + AusgabeVorschlagJsonDto.java (record)
    └── out/persistence/ausgabevorschlag/ (Entity, JpaRepository, Mapper, Adapter)
```

### Ports

```java
// port/out: abstrakte Quelle, kein File/Path in der Signatur
public interface AusgabeVorschlagQuellePort {
    List<ImportDokument> findeNeueDokumente();          // Referenz, Hash, Vorschläge
    void markiereVerarbeitet(ImportDokument dokument);  // → Ordner verarbeitet/
    void markiereFehlerhaft(ImportDokument dokument, String grund); // → Ordner fehler/
}

// port/in
public interface AusgabeVorschlagServicePort {
    List<AusgabeVorschlagBo> getAll(AusgabeVorschlagStatusBo status);
    AusgabeVorschlagBo getById(String id);
    void korrigieren(AusgabeVorschlagBo vorschlag);
    String genehmigen(String id);        // gibt die ID der neuen Ausgabe zurück
    void verwerfen(String id);
}
```

`ImportDokument` ist ein kleines Domain-Record: `referenz` (Dateiname), `hash` und `List<AusgabeVorschlagBo>`. Damit funktioniert der Service später auch mit einer anderen Quelle, zum Beispiel einem Upload oder einem Mail-Postfach.

### Domain-Objekt mit Regeln

`AusgabeVorschlagBo` enthält die Felder von `AusgabeBo` und zusätzlich `status`, `quelleReferenz`, `quelleHash`, `importiertAm`, `bearbeitetAm` und `ausgabeId`. Die Regeln gehören ins BO, nicht in den Service:

- `genehmigen()` und `verwerfen()` sind nur im Status `OFFEN` erlaubt, sonst gibt es eine Exception.
- `istVollstaendig()` prüft die Pflichtfelder von `ausgabe`: `datum`, `betrag`, `empfaenger` und `kategorie`.
- `toAusgabeBo()` erzeugt die definitive Ausgabe.

## 3. Ablauf

### Import (Scheduler)

1. `@Scheduled(cron = "${cashcontrol.import.cron}")` ruft `importPort.importieren()` auf.
2. Der Service holt die Dokumente aus der Quelle. Pro Dokument:
   - Ist `quelleHash` schon in der DB, wird das Dokument nur noch als verarbeitet markiert (keine Duplikate, siehe Kapitel 4).
   - Sonst werden die Vorschläge mit Status `OFFEN` gespeichert, und zwar **in einer eigenen Transaktion pro Datei**. Eine fehlerhafte Datei blockiert so die anderen nicht.
   - Erst nach dem Commit wird die Datei nach `verarbeitet/` verschoben. Schlägt das Verschieben fehl, verhindert der Hash beim nächsten Lauf ein Duplikat.
   - Bei ungültigem JSON landet die Datei in `fehler/`, mit Log-Eintrag.
3. Das Ergebnis (`importiert`, `übersprungen`, `fehlerhaft`) wird geloggt.

Dateien werden nicht gelöscht, sondern verschoben. So ist jeder Import nachvollziehbar.

### Genehmigen (UI)

```java
@Transactional
public String genehmigen(String id) {
    var vorschlag = persistencePort.getById(id);
    vorschlag.pruefeVollstaendig();
    var ausgabeId = ausgabeServicePort.createAusgabe(vorschlag.toAusgabeBo());
    vorschlag.genehmigen(ausgabeId);
    persistencePort.update(vorschlag);
    return ausgabeId;
}
```

**Wichtig:** Die Verbuchung läuft über `AusgabeServicePort.createAusgabe` und nicht direkt über die Persistenz. Nur so wird das `AusgabeCreatedEvent` ausgelöst, auf das `KostenEventConsumerAdapter` und `VergabungEventConsumerAdapter` im Modul `steuern` reagieren. Ausgabe und Statuswechsel laufen in derselben Transaktion.

## 4. Quell-Hash und Duplikaterkennung

### Was ist der Quell-Hash?

Der Quell-Hash ist ein **SHA-256-Fingerabdruck des Dateiinhalts**. Er wird über die rohen Bytes der JSON-Datei berechnet und als Hex-String mit 64 Zeichen gespeichert, zum Beispiel:

```
datei:  2026-11-07_migros.json
inhalt: [{"datum":"2026-11-05","betrag":84.30,"empfaenger":"Migros", ...}]
hash:   3f9a1c...e07b   (64 Hex-Zeichen)
```

Gleicher Inhalt ergibt immer den gleichen Hash. Schon ein geändertes Byte ergibt einen ganz anderen Hash. Der Dateiname spielt keine Rolle.

### Wozu braucht es ihn?

Die Hauptaufgabe ist die **Idempotenz**: Wird dieselbe Datei mehrfach eingelesen, entstehen trotzdem keine doppelten Vorschläge. Das kann in diesen Fällen passieren:

| Situation | Ohne Hash | Mit Hash |
|---|---|---|
| Vorschläge sind gespeichert, aber das Verschieben nach `verarbeitet/` schlägt fehl (z.B. fehlende Rechte am Bind-Mount, App-Absturz) | Der nächste Lauf importiert die Datei nochmals | Der nächste Lauf erkennt sie und verschiebt sie nur noch |
| Der Benutzer legt eine Datei aus `verarbeitet/` versehentlich zurück | Doppelter Import | Wird erkannt |
| Die gleiche Datei wird unter anderem Namen nochmals geliefert | Doppelter Import | Wird erkannt |

Der Dateiname allein reicht dafür nicht. Er kann sich ändern, und verschiedene Lieferungen können denselben Namen haben (z.B. `export.json`).

### Ablauf

1. Der `JsonDateiQuelleAdapter` liest die Datei **einmal** als `byte[]`.
2. Aus diesen Bytes berechnet er den Hash:
   ```java
   byte[] inhalt = Files.readAllBytes(pfad);
   String hash = HexFormat.of().formatHex(
           MessageDigest.getInstance("SHA-256").digest(inhalt));
   ```
3. Dieselben Bytes parst er mit Jackson zu `AusgabeVorschlagJsonDto`. Hash und Inhalt passen so garantiert zusammen, auch wenn jemand die Datei zwischendurch ändert.
4. Er gibt `ImportDokument(referenz, hash, vorschlaege)` zurück. Der Hash ist ein technisches Detail der Quelle, darum berechnet ihn der Adapter. Der Service braucht ihn nur als Schlüssel.
5. Der Service prüft mit `persistencePort.existiertQuelleHash(hash)`, ob die Datei schon importiert wurde:
   - **ja**: nichts speichern, nur `markiereVerarbeitet(...)` aufrufen und als «übersprungen» zählen.
   - **nein**: Vorschläge speichern, dann `markiereVerarbeitet(...)` aufrufen.
6. Zusätzlich sichert der Unique-Constraint `(quelle_hash, quelle_position)` in der DB ab. Laufen zwei Importe trotzdem parallel (z.B. Scheduler und manueller Button), scheitert der zweite am Constraint statt Duplikate zu erzeugen.

Damit `existiertQuelleHash` schnell bleibt, braucht es einen Index auf `quelle_hash`. Der Unique-Constraint `(quelle_hash, quelle_position)` deckt das ab, weil `quelle_hash` die erste Spalte ist.

### Quell-Position

Der Quell-Hash gilt für **die ganze Datei**. Eine JSON-Datei kann aber **mehrere Ausgaben** enthalten, zum Beispiel einen Kontoauszug als Array:

```json
[
  { "datum": "2026-11-03", "betrag": 84.30, "empfaenger": "Migros" },
  { "datum": "2026-11-04", "betrag": 12.00, "empfaenger": "SBB" },
  { "datum": "2026-11-05", "betrag": 84.30, "empfaenger": "Migros" }
]
```

Daraus entstehen drei Zeilen in `ausgabe_vorschlag`, und alle drei haben **denselben Hash**. `quelle_position` ist der Index des Eintrags im Array, beginnend bei 0:

| quelle_referenz | quelle_hash | quelle_position | empfaenger | betrag |
|---|---|---|---|---|
| auszug_nov.json | 3f9a…e07b | 0 | Migros | 84.30 |
| auszug_nov.json | 3f9a…e07b | 1 | SBB | 12.00 |
| auszug_nov.json | 3f9a…e07b | 2 | Migros | 84.30 |

Warum es die Spalte braucht:

1. **Ein eindeutiger Schlüssel pro Vorschlag.** Ein Unique-Constraint nur auf `quelle_hash` würde pro Datei nur eine Zeile zulassen. Erst die Kombination `(quelle_hash, quelle_position)` bezeichnet genau einen Eintrag aus genau einem Dateiinhalt. Beim zweiten Import derselben Datei scheitern alle drei Zeilen am Constraint, nicht nur eine.
2. **Gleiche Einträge in derselben Datei unterscheiden.** Position 0 und 2 im Beispiel sind inhaltlich identisch. Das sind zwei echte Einkäufe, keine Duplikate. Ein Hash pro Eintrag statt pro Datei würde sie als gleich erkennen und einen davon verwerfen. Die Position hält sie auseinander.
3. **Die Position ist stabil.** Gleicher Hash heisst gleiche Bytes, also auch gleiche Reihenfolge. Eintrag 1 ist darum bei jedem Einlesen derselben Datei wieder Eintrag 1. Ohne diese Garantie wäre die Position als Schlüssel nicht brauchbar.
4. **Nachvollziehbarkeit.** Im UI lässt sich zu jedem Vorschlag die Herkunft anzeigen, z.B. «auszug_nov.json, Eintrag 2». Mit der Position findet man den Eintrag in der archivierten Datei unter `verarbeitet/` wieder.

**Sonderfall: eine Ausgabe pro Datei.** Enthält eine Datei immer nur **ein** JSON-Objekt und kein Array, ist `quelle_position` immer 0. Steht das Format sicher fest, kann die Spalte wegfallen und der Constraint direkt auf `quelle_hash` liegen. Die Spalte sollte trotzdem bleiben. Sie kostet fast nichts, und der Import funktioniert dann auch, falls später Sammeldateien kommen. Das hängt von der offenen Frage 1 ab (Format der Dateien).

### Grenzen

- **Nur inhaltsgleiche Dateien werden erkannt.** Wird eine Datei neu erzeugt und hat dabei andere Formatierung, Reihenfolge oder einen Zeitstempel im Inhalt, ergibt das einen neuen Hash und damit einen neuen Import. Der Hash schützt gegen *technische* Wiederholungen, nicht gegen *fachliche* Duplikate.
- **Fachliche Duplikate** (gleiche Ausgabe in zwei verschiedenen Dateien) lassen sich über einen zweiten Schlüssel aus `datum`, `betrag` und `empfaenger` finden. Dieser Schlüssel soll aber **nur eine Warnung im UI** auslösen («möglicherweise schon erfasst»), keine Sperre. Zwei identische Einkäufe am selben Tag sind durchaus möglich.
- **Halb geschriebene Dateien:** Schreibt der Lieferant noch an der Datei, während der Scheduler läuft, entstehen ein falscher Hash und ungültiges JSON. Die Datei landet dann in `fehler/`. Abhilfe: Der Lieferant schreibt zuerst `*.tmp` und benennt die Datei danach in `*.json` um. Der Adapter liest nur `*.json`. Alternativ liest der Adapter nur Dateien, die seit mindestens einer Minute nicht mehr geändert wurden.

## 5. Datenbank (neue Migration `V002__create_ausgabe_vorschlag.sql`)

```sql
create table ausgabe_vorschlag (
    id              varchar(255)  not null primary key,
    status          varchar(20)   not null,          -- OFFEN | GENEHMIGT | VERWORFEN
    datum           date,                            -- bewusst nullable: Vorschlag darf
    betrag          decimal,                         -- unvollständig sein, der Benutzer
    zahlender       varchar(255),                    -- ergänzt ihn im UI
    empfaenger      varchar(255),
    kategorie       varchar(255),
    text            varchar(255),
    quelle_referenz varchar(255)  not null,          -- Dateiname
    quelle_hash     varchar(64)   not null,          -- SHA-256 des Dateiinhalts, siehe Kapitel 4
    quelle_position integer       not null,          -- Index, falls eine Datei mehrere enthält
    importiert_am   timestamp     not null,
    bearbeitet_am   timestamp,
    ausgabe_id      varchar(255),
    constraint uq_ausgabe_vorschlag_quelle unique (quelle_hash, quelle_position),
    constraint fk_ausgabe_vorschlag_ausgabe foreign key (ausgabe_id)
        references ausgabe(id) on delete set null,
    constraint fk_ausgabe_vorschlag_zahlender foreign key (zahlender)
        references person(id) on delete restrict
);
create index idx_ausgabe_vorschlag_status on ausgabe_vorschlag (status);
```

Ich schlage eine separate Tabelle mit Status vor, statt einem Flag `entwurf` in `ausgabe`. So bleiben `ausgabe`, die Views in `R__create_views.sql` und das Reporting frei von unbestätigten Daten.

## 6. Konfiguration und Docker

`application.yml`:

```yaml
cashcontrol:
  import:
    aktiv: true
    verzeichnis: /import
    cron: "0 */15 * * * *"
```

- Eine `SchedulingConfig` mit `@EnableScheduling` und `@ConditionalOnProperty("cashcontrol.import.aktiv")` gehört nach `config/`. In Tests lässt sich der Import so abschalten.
- In `application-local.yml` zeigt `verzeichnis` auf einen Ordner auf dem Mac.
- In `betrieb/docker-compose.yml` kommt beim Service `app` ein Bind-Mount dazu:
  ```yaml
  volumes:
    - ${IMPORT_VERZEICHNIS:?In .env setzen}:/import
  ```
  und `IMPORT_VERZEICHNIS` in `.env.example`. Im Container sind darunter `verarbeitet/` und `fehler/` nötig. Der Adapter legt sie bei Bedarf an.
- Es läuft nur eine App-Instanz, darum braucht es keine Sperre gegen parallele Läufe (z.B. ShedLock).

## 7. REST-API (contract-first in `api-spec/cashcontrol.yaml`)

| Methode | Pfad | Zweck |
|---|---|---|
| GET | `/ausgabe-vorschlaege?status=OFFEN` | Liste |
| GET | `/ausgabe-vorschlaege/{id}` | Detail |
| PUT | `/ausgabe-vorschlaege/{id}` | Korrigieren (Kategorie, Zahlender usw.) |
| POST | `/ausgabe-vorschlaege/{id}/genehmigen` | Verbuchen, gibt Ausgabe-ID zurück |
| POST | `/ausgabe-vorschlaege/{id}/verwerfen` | Verwerfen |
| POST | `/ausgabe-vorschlaege/import` | Import manuell auslösen (gleicher In-Port wie der Scheduler) |

Der manuelle Import ist praktisch zum Testen und für einen Button «Jetzt einlesen» im UI.

## 8. Frontend

Ein neuer Bereich `domains/ausgabe-vorschlag/` nach dem bestehenden Muster:

- `ausgabe-vorschlag-list`: Tabelle der offenen Vorschläge. Unvollständige Vorschläge sind markiert, pro Zeile gibt es «Genehmigen» und «Verwerfen». Optional: Mehrfachauswahl für Sammelfreigabe.
- `ausgabe-vorschlag-edit`: Formular wie `ausgabe-edit`, mit den Buttons «Speichern» und «Speichern & genehmigen».
- Optional: Ein Badge in der Toolbar zeigt die Zahl der offenen Vorschläge.

## 9. Tests

- `JsonDateiQuelleAdapter`: mit `@TempDir`. Testfälle: gültige und ungültige Datei, Verschieben nach `verarbeitet/` und `fehler/`, gleicher Hash bei gleichem Inhalt unter anderem Dateinamen.
- Services: Unit-Tests mit Fakes der Out-Ports. Testfälle: Duplikat per Hash, Statusregeln, Genehmigen ruft `createAusgabe` auf.
- Ende zu Ende mit Testcontainers: Datei ins Verzeichnis legen, Import auslösen, Vorschlag genehmigen. Danach muss es eine `ausgabe` geben und das Event muss in `steuern` angekommen sein.

## 10. Offene Fragen vor der Umsetzung

1. **JSON-Format:** Woher kommen die Dateien (Bank-Export, OCR, Skript)? Enthält eine Datei einen Eintrag oder eine Liste?
2. **Kategorie und Zahlender:** Kommen diese als Code bzw. Person-ID oder als Freitext? Bei Freitext braucht der Import eine Zuordnung, z.B. über `PersonPersistencePort` oder eine Regeltabelle. Was nicht zugeordnet werden kann, bleibt leer und wird im UI ergänzt.
3. **Verworfene Vorschläge:** Behalten, damit nachvollziehbar bleibt, was abgelehnt wurde? Oder nach einer gewissen Zeit löschen?
4. **Modulgrenze:** Der Import ist in `finanzen` vorgesehen, weil er `AusgabeBo` und `AusgabeKategorieBo` direkt verwendet. Soll er später mehr als nur Ausgaben erzeugen, lohnt sich ein eigenes Modul `modules/import`, das nur über `AusgabeServicePort` auf `finanzen` zugreift.
