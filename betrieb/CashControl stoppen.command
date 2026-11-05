#!/bin/zsh
# Stoppt App und Datenbank. Die Daten bleiben erhalten.

source "${0:A:h}/_gemeinsam.zsh"

pruefe_env

if ! docker info >/dev/null 2>&1; then
  echo "Docker laeuft nicht, es gibt nichts zu stoppen."
  exit 0
fi

docker compose stop
echo "CashControl ist gestoppt."
