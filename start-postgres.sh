#!/usr/bin/env bash
################################################################################
# start-postgres.sh
#
# A script to:
#   1) Ensure required packages are installed (Debian/Ubuntu).
#   2) Install Docker Engine if missing.
#   3) In WSL: first attempt systemctl, fallback to service if "System has not
#      been booted with systemd" or other systemd error occurs.
#   4) On normal systemd environment: just systemctl.
#   5) **STOP & REMOVE any existing Postgres container**, then pull & run a fresh one,
#      automatically creating a database named "security_standards_db".
################################################################################

set -euo pipefail

###############################################################################
# CONFIGURATION
###############################################################################
POSTGRES_CONTAINER_NAME="my-postgres"
POSTGRES_IMAGE="postgres:latest"
POSTGRES_PORT="5432"
POSTGRES_USER="postgres"
POSTGRES_PASSWORD="postgres"
POSTGRES_DB="security_standards_db"

REQUIRED_PKGS=(
  apt-transport-https
  ca-certificates
  curl
  gnupg
  lsb-release
)

###############################################################################
# Function: ensure_package_installed
###############################################################################
ensure_package_installed() {
  local pkg="$1"
  if dpkg -s "$pkg" &>/dev/null; then
    echo "[OK] Package '$pkg' is already installed."
  else
    echo "[INFO] Installing package: $pkg"
    sudo apt-get install -y "$pkg"
  fi
}

###############################################################################
# Function: ensure_required_software
###############################################################################
ensure_required_software() {
  echo "Ensuring required packages are installed..."
  sudo apt-get update -y
  for pkg in "${REQUIRED_PKGS[@]}"; do
    ensure_package_installed "$pkg"
  done
  echo "[OK] All required packages are installed."
}

###############################################################################
# Function: check_docker_installed
###############################################################################
check_docker_installed() {
  if command -v docker &>/dev/null; then
    return 0
  else
    return 1
  fi
}

###############################################################################
# Function: install_docker
###############################################################################
install_docker() {
  echo "[INFO] Installing Docker Engine..."

  # Docker’s official GPG key
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
    | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

  # Add Docker stable repo
  echo \
    "deb [arch=$(dpkg --print-architecture) \
     signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] \
    https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) stable" \
    | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

  sudo apt-get update -y
  sudo apt-get install -y docker-ce docker-ce-cli containerd.io

  echo "[OK] Docker installed successfully."
}

###############################################################################
# Function: try_systemctl_start_docker
#   Attempts systemctl is-active/start Docker. 
#   Returns 0 if successful, 1 if systemctl fails or Docker can't be started.
###############################################################################
try_systemctl_start_docker() {
  echo "[INFO] Attempting to use systemctl to start Docker..."
  if ! sudo systemctl is-active docker &>/dev/null; then
    if sudo systemctl start docker; then
      echo "[INFO] Docker service started via systemctl."
      return 0
    else
      echo "[WARN] systemctl start docker failed."
      return 1
    fi
  else
    echo "[OK] Docker service is already running (systemctl)."
    return 0
  fi
}

###############################################################################
# Function: try_service_start_docker
#   Attempts "service docker start".
###############################################################################
try_service_start_docker() {
  echo "[INFO] Attempting 'service docker start'..."
  if command -v service &>/dev/null; then
    if sudo service docker start; then
      echo "[INFO] Docker service started via 'service'."
      return 0
    else
      echo "[WARN] 'service docker start' failed."
      return 1
    fi
  else
    echo "[ERROR] 'service' command not found. Cannot start Docker this way."
    return 1
  fi
}

###############################################################################
# Function: start_docker_service
#   - On WSL: try systemctl, fallback to service if needed.
#   - On normal systemd environment: use systemctl.
###############################################################################
start_docker_service() {
  if grep -qEi "(Microsoft|WSL)" /proc/version; then
    echo "[INFO] Detected WSL environment."

    if command -v systemctl &>/dev/null; then
      # Try systemctl; if fails, fallback to service
      if ! try_systemctl_start_docker; then
        echo "[INFO] Falling back to 'service docker start'."
        if ! try_service_start_docker; then
          echo "[ERROR] Neither systemctl nor 'service docker start' worked in WSL."
          echo "[INFO] Please start Docker manually (e.g. Docker Desktop)."
        fi
      fi
    else
      # systemctl not available
      if ! try_service_start_docker; then
        echo "[ERROR] 'service docker start' also failed. Please start Docker manually in WSL."
      fi
    fi
  else
    echo "[INFO] Non-WSL environment: using systemd."
    try_systemctl_start_docker || {
      echo "[ERROR] Could not start Docker with systemctl on non-WSL environment. Please investigate."
    }
  fi
}

###############################################################################
# Function: run_postgres_container
#
#   1) Stop & remove any existing container named "${POSTGRES_CONTAINER_NAME}"
#   2) Pull the latest postgres image
#   3) Run a brand-new container, automatically creating ${POSTGRES_DB}
###############################################################################
run_postgres_container() {
  echo "[INFO] Ensuring no existing Postgres container named '${POSTGRES_CONTAINER_NAME}'..."
  if docker ps -a --format '{{.Names}}' | grep -Eq "^${POSTGRES_CONTAINER_NAME}\$"; then
    echo "[INFO] Stopping and removing existing container: ${POSTGRES_CONTAINER_NAME}"
    docker stop "${POSTGRES_CONTAINER_NAME}" || true
    docker rm "${POSTGRES_CONTAINER_NAME}" || true
  fi

  echo "[INFO] Pulling PostgreSQL image: ${POSTGRES_IMAGE}"
  docker pull "${POSTGRES_IMAGE}"

  echo "[INFO] Running a new Postgres container '${POSTGRES_CONTAINER_NAME}'..."
  docker run -d \
    --name "${POSTGRES_CONTAINER_NAME}" \
    -p "${POSTGRES_PORT}:5432" \
    -e POSTGRES_USER="${POSTGRES_USER}" \
    -e POSTGRES_PASSWORD="${POSTGRES_PASSWORD}" \
    -e POSTGRES_DB="${POSTGRES_DB}" \
    "${POSTGRES_IMAGE}"
}

###############################################################################
# MAIN SCRIPT
###############################################################################
echo "=== Starting Postgres Local Setup Script ==="

# 1) Ensure required software is installed
ensure_required_software

# 2) Check Docker
if check_docker_installed; then
  echo "[OK] Docker is already installed."
else
  install_docker
fi

# 3) Attempt to start Docker
start_docker_service

###############################################################################
# 3a) WAIT AND RE-CHECK
#     Introduce a brief wait + re-check to ensure Docker is actually up
###############################################################################
echo "[INFO] Waiting briefly for Docker to be fully available..."
# Give Docker up to 5 tries (10 seconds total) to respond
for i in {1..5}; do
  if docker info &>/dev/null; then
    echo "[OK] Docker is now responsive."
    break
  fi
  echo "[INFO] Docker not ready yet (attempt $i/5). Waiting 2s..."
  sleep 2
done

# If it’s *still* not up, abort
if ! docker info &>/dev/null; then
  echo "[ERROR] Docker did not become ready in time. Please check Docker status and try again."
  exit 1
fi

# 4) If Docker is running, remove any old Postgres container & run a new one
run_postgres_container

cat <<EOF

--------------------------------------------------------------------------------
PostgreSQL container info:
  Container Name: ${POSTGRES_CONTAINER_NAME}
  Image:          ${POSTGRES_IMAGE}
  Port Mapping:   Host ${POSTGRES_PORT} -> Container 5432
  Credentials:    user=${POSTGRES_USER}, password=${POSTGRES_PASSWORD}
  Database:       ${POSTGRES_DB}

Connect with:
  psql -h 127.0.0.1 -p ${POSTGRES_PORT} -U ${POSTGRES_USER} -d ${POSTGRES_DB}

Stop container:
  docker stop ${POSTGRES_CONTAINER_NAME}

Remove container entirely:
  docker rm ${POSTGRES_CONTAINER_NAME}

Each run of this script will ALWAYS remove and recreate the container,
ensuring a fresh PostgreSQL database every time.

Logs:
  docker logs -f ${POSTGRES_CONTAINER_NAME}
--------------------------------------------------------------------------------
EOF

exit 0
