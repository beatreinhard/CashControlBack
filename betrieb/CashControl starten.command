#!/bin/zsh
# Startet Datenbank und App und oeffnet CashControl im Browser.

source "${0:A:h}/_gemeinsam.zsh"

URL="http://localhost:8080/cash-control"

pruefe_env
warte_auf_docker

docker image inspect cashcontrol:latest >/dev/null 2>&1 \
  || fehler "Das Image cashcontrol:latest fehlt. Bitte im Repo bauen, siehe README."

echo "CashControl wird gestartet ..."
docker compose up -d

echo "Warte auf die App ..."
for i in {1..120}; do
  if curl -s -o /dev/null --max-time 2 "$URL"; then
    echo "CashControl laeuft: $URL"
    open "$URL"
    exit 0
  fi
  sleep 1
done

docker compose logs --tail 30 app
fehler "Die App antwortet nach 2 Minuten nicht, siehe Log oben."
