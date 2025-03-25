#!/usr/bin/env bash
################################################################################
# stop-postgres.sh
#
# Stops and removes all Docker containers.
################################################################################

set -euo pipefail

echo "=== Stopping and removing all Docker containers ==="

# Capture all container IDs (both running and stopped)
ALL_CONTAINERS="$(docker ps -aq)"

# Check if there are any containers at all
if [ -z "$ALL_CONTAINERS" ]; then
  echo "[INFO] No containers found to stop or remove."
else
  echo "[INFO] Stopping all containers..."
  docker stop $ALL_CONTAINERS
  echo "[OK] All containers have been stopped."

  echo "[INFO] Removing all containers..."
  docker rm $ALL_CONTAINERS
  echo "[OK] All containers have been removed."
fi

echo "[DONE] Operation complete."
exit 0
