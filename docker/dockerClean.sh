#!/usr/bin/env bash

# Остановить приложение
docker compose rm -f -v
# Удаляем volume
docker volume rm docker_db-data