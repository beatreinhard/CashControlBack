# CashControl

## Build

Der Build läuft in zwei Schritten: Maven erstellt ein ausführbares Jar, das auch das Frontend enthält. Dieses Jar wird anschliessend in ein Docker-Image gepackt.

### Voraussetzungen

- Java 21
- Docker: für die Testcontainers-Tests und das Image

Maven muss nicht installiert sein, der Wrapper `backend/mvnw` lädt die passende Version selbst. Auch Node und npm werden beim Build automatisch heruntergeladen.

### Jar bauen

Im Repo-Root ausführen:

```bash
sh backend/mvnw clean install
```

Der Build läuft in dieser Reihenfolge:

1. `frontend`: baut die Angular-App nach `frontend/dist/frontend/browser`.
2. `backend`: kopiert das Frontend nach `classes/static`, kompiliert das Backend, führt die Tests aus und erstellt das Jar.

Ergebnis in `backend/target/`:

| Datei | Inhalt |
|---|---|
| `backend-1.0.0-SNAPSHOT-exec.jar` | Ausführbares Jar mit Frontend und allen Abhängigkeiten |
| `backend-1.0.0-SNAPSHOT.jar` | Nicht ausführbar, enthält keine Abhängigkeiten und wird von den Integrationstests verwendet |

Der Suffix `-exec` ist in `backend/pom.xml` als `classifier` des `spring-boot-maven-plugin` definiert.

Ohne Tests bauen:

```bash
sh backend/mvnw clean install -DskipTests
```

### Docker-Image bauen

Das Image enthält nur das `-exec`-Jar, gebaut wird es nicht im Docker-Build. Darum zuerst das Jar bauen, dann im Repo-Root ausführen:

```bash
docker build -t cashcontrol .
```

> **Wichtig:** Ohne vorherigen Maven-Build landet das zuletzt gebaute Jar im Image. Codeänderungen fehlen dann.

## Start

| Dienst | Lokal (Profil `local`) | Betrieb (Docker) |
|---|---|---|
| App | 8081 | 8080 |
| Frontend | 4200 | 8080, in der App enthalten |
| Datenbank | 5431 | nicht freigegeben |

Die Ports gelten auf dem eigenen Rechner. Lokal und Betrieb haben je eine eigene Datenbank. Im Betrieb ist die App nur auf diesem Mac erreichbar. Die Datenbank hat keinen freigegebenen Port, die App erreicht sie innerhalb von Docker über `postgres-betrieb:5432`.

### Betrieb

Der Betrieb liegt im Verzeichnis `betrieb/`. Dieses Verzeichnis lässt sich an einen beliebigen Ort auf dem Mac kopieren und von dort benutzen.

| Datei | Zweck |
|---|---|
| `CashControl starten.command` | Startet Docker Desktop, Datenbank und App und öffnet die App im Browser |
| `CashControl stoppen.command` | Stoppt App und Datenbank, die Daten bleiben erhalten |
| `CashControl Backup.command` | Schreibt ein Backup der Datenbank nach `backups/` |
| `docker-compose.yml` | Definiert App und Betriebs-Datenbank |
| `.env` | Passwort der Datenbank und Compose-Projektname, liegt nicht im Git |
| `_gemeinsam.zsh` | Hilfsfunktionen der Scripts |

Die `.command`-Dateien werden im Finder per Doppelklick gestartet.

#### Einrichten

1. Jar und Image bauen, siehe [Build](#build). Das Image liegt danach in Docker, nicht im Verzeichnis `betrieb/`.
2. Im Verzeichnis `betrieb/` die Datei `.env` anlegen und das Passwort setzen:

   ```bash
   cp .env.example .env
   ```

3. Das Verzeichnis `betrieb/` an den gewünschten Ort kopieren. `.env` ist im Finder versteckt, wird aber mitkopiert.

Postgres übernimmt das Passwort nur beim ersten Anlegen der Datenbank. Ein späterer Wechsel braucht zusätzlich ein `ALTER USER` in der Datenbank.

#### Neue Version einspielen

Im Repo-Root Jar und Image neu bauen und CashControl neu starten. Der kopierte Ordner bleibt dabei unverändert, Compose erkennt das neue Image beim Start selbst:

```bash
sh backend/mvnw clean install
docker build -t cashcontrol .
```

Ändert sich `betrieb/docker-compose.yml` oder ein Script im Repo, muss die Datei von Hand an den Einsatzort kopiert werden.

Die Datenbank-Migrationen laufen beim Start der App automatisch mit Flyway.

#### Erreichbarkeit

| Was | URL |
|---|---|
| Frontend | http://localhost:8080/cash-control |
| Swagger UI | http://localhost:8080/cash-control/swagger-ui/index.html |
| OpenAPI-Spec | http://localhost:8080/cash-control/v3/api-docs |

#### Daten und Backup

Die Daten liegen im Docker-Volume `cashcontrol-db-betrieb`, nicht im kopierten Verzeichnis. Sie überstehen `docker compose down`. Mit `docker compose down -v` oder einem Zurücksetzen von Docker Desktop werden sie gelöscht.

Das Backup läuft auch bei gestoppter App. Die Dateien in `backups/` enthalten die Daten unverschlüsselt und werden nie automatisch gelöscht. Sie liegen auf demselben Mac, darum eine Kopie zusätzlich extern ablegen.

Backup einspielen: App stoppen und im kopierten Verzeichnis ausführen. Dabei werden die aktuellen Daten ersetzt:

```bash
docker compose up -d postgres-betrieb
gunzip -c backups/cashcontrol-DATUM.sql.gz | docker compose exec -T postgres-betrieb psql -q -v ON_ERROR_STOP=1 -U cashcontrol -d cashcontrol
```

### Lokale Entwicklung

Hier läuft nur die lokale Datenbank in Docker, Backend und Frontend laufen lokal.

1. Lokale Datenbank starten, im Ordner `docker/`:

   ```bash
   docker compose up
   ```

2. Backend mit dem Spring-Profil `local` starten: in IntelliJ bei `CashcontrolApplication` unter *Active profiles* `local` eintragen oder im Repo-Root:

   ```bash
   java -jar backend/target/backend-1.0.0-SNAPSHOT-exec.jar --spring.profiles.active=local
   ```

3. Frontend mit Live-Reload starten, im Ordner `frontend/`:

   ```bash
   npm start
   ```

Das Frontend ist dann unter http://localhost:4200 erreichbar. Anfragen an `/cash-control` leitet es an das Backend auf Port 8081 weiter.

Die lokale Datenbank beenden, im Ordner `docker/`:

```bash
docker compose stop
```

In IntelliJ startet die Run-Config `docker: Compose Deployment` diese Datenbank.

Die Daten liegen im Volume `cashcontrol-db-dev`. Sie überstehen `docker compose down`, erst mit `-v` werden sie gelöscht.

`docker/.env` setzt den Compose-Projektnamen `cashcontrol`, damit das Projekt von anderen Apps mit einem Ordner `docker/` getrennt bleibt. Die Datei enthält keine Geheimnisse und liegt im Git.

### Konfiguration

| Datei | Zweck |
|---|---|
| `application.yml` | Gilt immer und enthält die Werte für den Betrieb, aber keine DB-Zugangsdaten |
| `application-local.yml` | Überschreibt nur die Werte für die Entwicklung: lokale DB-Zugangsdaten, SQL- und DEBUG-Logging, Thymeleaf-Templates ohne Cache |

Das Profil `local` ist nur aktiv, wenn es beim Start gesetzt wird. Das Docker-Image läuft deshalb ohne dieses Profil.

Ohne das Profil `local` braucht die App die DB-Zugangsdaten als Umgebungsvariablen, sonst startet sie nicht:

| Variable | Beispiel |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres-betrieb:5432/cashcontrol` |
| `SPRING_DATASOURCE_USERNAME` | `cashcontrol` |
| `SPRING_DATASOURCE_PASSWORD` | Passwort des DB-Benutzers, in Docker aus `betrieb/.env` |

In `betrieb/docker-compose.yml` sind diese Variablen für den Service `app` bereits gesetzt.
