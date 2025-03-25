#!/usr/bin/env bash
################################################################################
# remove-postgres.sh
#
# Removes the 'my-postgres' Docker container if it exists.
# If the container is running, we stop it first.
################################################################################

set -euo pipefail

CONTAINER_NAME="my-postgres"

echo "=== Removing Postgres Container: $CONTAINER_NAME ==="

# If running, stop it first
if docker ps --format '{{.Names}}' | grep -Eq "^${CONTAINER_NAME}\$"; then
  echo "[INFO] Container '$CONTAINER_NAME' is running. Stopping it first..."
  docker stop "$CONTAINER_NAME"
fi

# If it exists (stopped or otherwise), remove it
if docker ps -a --format '{{.Names}}' | grep -Eq "^${CONTAINER_NAME}\$"; then
  echo "[INFO] Removing container '$CONTAINER_NAME'..."
  docker rm "$CONTAINER_NAME"
  echo "[OK] Container '$CONTAINER_NAME' removed."
else
  echo "[OK] Container '$CONTAINER_NAME' does not exist."
fi
