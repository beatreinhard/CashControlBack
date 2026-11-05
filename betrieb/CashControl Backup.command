#!/bin/zsh
# Schreibt ein Backup der Betriebs-Datenbank nach backups/.
# Laeuft auch, wenn CashControl gestoppt ist: Die Datenbank wird dann nur dafuer kurz gestartet.

source "${0:A:h}/_gemeinsam.zsh"

pruefe_env
warte_auf_docker

DATEI="backups/cashcontrol-$(date +%Y-%m-%d_%H%M%S).sql.gz"
TEMP="$DATEI.tmp"
DB_WAR_GESTOPPT=false

stoppe_db_wieder() {
  rm -f "$TEMP"
  if [[ $DB_WAR_GESTOPPT == true ]]; then
    docker compose stop postgres-betrieb >/dev/null 2>&1 || true
  fi
}
trap stoppe_db_wieder EXIT

if [[ "$(docker inspect -f '{{.State.Running}}' cashcontrol-db-betrieb 2>/dev/null)" != "true" ]]; then
  DB_WAR_GESTOPPT=true
  docker compose up -d postgres-betrieb
fi
warte_auf_datenbank

mkdir -p backups
chmod 700 backups

echo "Backup wird erstellt ..."
docker compose exec -T postgres-betrieb \
  pg_dump -U cashcontrol -d cashcontrol --clean --if-exists | gzip > "$TEMP"

# Erst nach einem fehlerfreien Dump erscheint die Datei unter ihrem endgueltigen Namen.
mv "$TEMP" "$DATEI"
chmod 600 "$DATEI"
echo "Backup erstellt: ${DATEI:A} ($(du -h "$DATEI" | cut -f1 | tr -d ' '))"
