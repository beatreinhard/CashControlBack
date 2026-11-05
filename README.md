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
| Datenbank | 5431 | 5432 |

Die Ports gelten auf dem eigenen Rechner. Lokal und Betrieb haben je eine eigene Datenbank. Innerhalb von Docker erreicht die App ihre Datenbank über `postgres-betrieb:5432`.

### Komplett in Docker

Startet die Betriebs-Datenbank und die App. Im Repo-Root zuerst Jar und Image bauen, sonst läuft die App mit einem alten Stand:

```bash
sh backend/mvnw clean install
docker build -t cashcontrol .
```

Beim ersten Mal im Repo-Root die Datei `docker/.env` anlegen und darin das Passwort der Betriebs-Datenbank setzen:

```bash
cp docker/.env.example docker/.env
```

Dann im Ordner `docker/` ausführen:

```bash
docker compose --profile betrieb up
```

Das Profil `betrieb` startet die App und die Betriebs-Datenbank. Ohne Profil startet Compose nichts. Compose baut selbst nichts, sondern startet das vorhandene Image `cashcontrol:latest`.

Die App ist danach hier erreichbar:

| Was | URL |
|---|---|
| Frontend | http://localhost:8080/cash-control |
| Swagger UI | http://localhost:8080/cash-control/swagger-ui/index.html |
| OpenAPI-Spec | http://localhost:8080/cash-control/v3/api-docs |

Die Datenbank-Migrationen laufen beim Start der App automatisch mit Flyway.

Beenden im Ordner `docker/`:

```bash
docker compose --profile betrieb stop
```

> **Achtung:** Die Daten liegen in den Volumes `cashcontrol-db-dev` und `cashcontrol-db-betrieb` und überstehen `docker compose --profile dev down` und `docker compose --profile betrieb down`. Erst mit `-v` werden sie gelöscht.

### Lokale Entwicklung

Hier läuft nur die lokale Datenbank in Docker, Backend und Frontend laufen lokal.

1. Lokale Datenbank starten, im Ordner `docker/`. Das Compose-Profil `dev` startet nur diese Datenbank:

   ```bash
   docker compose --profile dev up
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
docker compose --profile dev stop
```

Wird die Datenbank aus IntelliJ gestartet, braucht die Run-Config `docker: Compose Deployment` die Umgebungsvariable `COMPOSE_PROFILES=dev`.

> **Hinweis:** Es gibt zwei Arten von Profilen. `dev` und `betrieb` sind Compose-Profile und bestimmen, welche Container starten. `local` ist ein Spring-Profil und bestimmt die Konfiguration der App.

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
| `SPRING_DATASOURCE_PASSWORD` | Passwort des DB-Benutzers, in Docker aus `docker/.env` |

Im `docker-compose.yml` sind diese Variablen für den Service `app` bereits gesetzt.

`docker/.env` liegt nicht im Git und wird für jeden `docker compose`-Befehl gebraucht, auch für die lokale Datenbank. Fehlt die Datei, bricht Compose mit einer Fehlermeldung ab.

Postgres übernimmt das Passwort nur beim ersten Anlegen der Datenbank. Ein späterer Wechsel braucht zusätzlich ein `ALTER USER` in der Datenbank.

`docker/.env` setzt ausserdem den Compose-Projektnamen `cashcontrol`. So bleibt das Projekt von anderen Apps mit einem Ordner `docker/` getrennt.
