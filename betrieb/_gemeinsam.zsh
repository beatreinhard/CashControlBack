# Wird von den .command-Dateien eingebunden, nicht direkt starten.

set -e
setopt pipefail

# Doppelklick startet im Home-Verzeichnis, Compose braucht aber diesen Ordner.
cd "${0:A:h}"

fehler() {
  echo
  echo "FEHLER: $1"
  exit 1
}

pruefe_env() {
  [[ -f .env ]] || fehler "Die Datei .env fehlt. .env.example nach .env kopieren und das Passwort setzen."
}

warte_auf_docker() {
  docker info >/dev/null 2>&1 && return 0

  echo "Docker Desktop wird gestartet ..."
  open -a Docker || fehler "Docker Desktop konnte nicht gestartet werden."

  local i
  for i in {1..120}; do
    docker info >/dev/null 2>&1 && return 0
    sleep 1
  done
  fehler "Docker Desktop ist nach 2 Minuten nicht bereit."
}

warte_auf_datenbank() {
  local i
  for i in {1..60}; do
    docker compose exec -T postgres-betrieb pg_isready -U cashcontrol -d cashcontrol >/dev/null 2>&1 && return 0
    sleep 1
  done
  fehler "Die Datenbank ist nach 1 Minute nicht bereit."
}
