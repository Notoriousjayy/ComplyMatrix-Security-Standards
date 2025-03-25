# ComplyMatrix Security Standards Database

This repository hosts a **Spring Boot** application and a **PostgreSQL**-backed database schema for managing security standards, control requirements, compliance tracking, and more. It uses **Liquibase** for database change management, **Maven** for build automation, and **Testcontainers** for integration testing.

---

## Table of Contents

- [Overview](#overview)
- [Architecture Diagram](#architecture-diagram)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Local Development](#local-development)
  - [1. Start Local PostgreSQL](#1-start-local-postgresql)
  - [2. Build the Project](#2-build-the-project)
  - [3. Run Liquibase Migrations](#3-run-liquibase-migrations)
  - [4. (Optional) Run the Application](#4-optional-run-the-application)
- [Testing](#testing)
- [Liquibase Changelogs](#liquibase-changelogs)
- [Scripts](#scripts)
  - [start-postgres.sh](#start-postgressh)
  - [stop-postgres.sh](#stop-postgressh)
  - [remove-postgres.sh](#remove-postgressh)
- [Typical Database Creation Order](#typical-database-creation-order)
- [Using Profiles and Environments](#using-profiles-and-environments)
- [Rollback and Recovery](#rollback-and-recovery)
- [API Documentation (OpenAPI 3.0)](#api-documentation-openapi-30)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

**ComplyMatrix** is designed to streamline the management of regulatory standards and requirements (e.g., ISO 27001, NIST CSF, PCI-DSS) within organizations. The schema includes tables for standards, controls, training resources, organizations, and bridging tables to track compliance and relevant mappings.

Key technologies:

- **Spring Boot**: Main application framework.
- **Liquibase**: Database change management and versioning.
- **PostgreSQL**: Primary database (local or on AWS RDS).
- **Testcontainers**: Integration tests with real PostgreSQL containers.
- **Maven**: Build and dependency management.

---

## Architecture Diagram

```
+----------------------+        +---------------------+
| Developer           |        |                     |
| (writes code, YAML) |  --->  |  Git Repo / CI Tool |
+----------------------+        +---------------------+
                                      |
                                      v
                          +-------------------------+
                          |   Maven Build + Tests   |
                          |   (Liquibase, JUnit)    |
                          +-------------------------+
                                      |
                                      v
                           +----------------------+
                           |   PostgreSQL DB      |
                           | (Local / AWS RDS)    |
                           +----------------------+
```

---

## Features

1. **Entity Modeling**: JPA entities for `Standards`, `ControlRequirements`, `Organizations`, etc.
2. **Liquibase**: Changelogs define schema objects (tables, triggers, views) plus data insertion.
3. **Testcontainers**: Spinning up PostgreSQL in integration tests ensures consistent test environments.
4. **Scripts**: Shell scripts to spin up or tear down local PostgreSQL containers.
5. **OpenAPI Specification**: A comprehensive API definition for all endpoints.

---

## Project Structure

```
.
├── pom.xml
├── Readme.md
├── remove-postgres.sh
├── start-postgres.sh
├── stop-postgres.sh
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── complymatrix
│   │   │           └── app
│   │   │               ├── Application.java
│   │   │               ├── entity/              // JPA entities
│   │   │               ├── repository/          // Spring Data Repositories
│   │   │               └── service/
│   │   └── resources
│   │       ├── application.properties
│   │       ├── liquibase
│   │       │   ├── changelog-master.xml
│   │       │   ├── changesets/                  // Each changeset file
│   │       │   └── data-inserts/
│   │       └── openapi
│   │           └── openapi.yaml                 // OpenAPI 3.0 specification
│   └── test
│       ├── java
│       │   └── com
│       │       └── complymatrix
│       │           └── app
│       │               ├── ApplicationTest.java
│       │               ├── BasePostgresContainerTest.java
│       │               └── repository/          // Repository integration tests
│       └── resources
│           └── application-test.properties
└── target
    └── ... // build outputs
```

**Notable files and directories**:

- **`Application.java`**: Spring Boot entry point.
- **`pom.xml`**: Maven build config (including Liquibase plugin).
- **`liquibase/`**: Contains the master changelog and all changeset files.
- **`openapi.yaml`**: OpenAPI 3.0 definition for all REST endpoints. Bundled in the JAR for optional serving at runtime.
- **`start-postgres.sh`, `stop-postgres.sh`, `remove-postgres.sh`**: Scripts to manage local Docker containers of PostgreSQL.
- **`BasePostgresContainerTest.java`**: Manages Testcontainers logic for integration tests.

---

## Prerequisites

1. **Java 11+** (JDK)
2. **Maven 3+**
3. **Docker** (for local PostgreSQL via `start-postgres.sh`, or you can use your own local Postgres installation)
4. **Git** (to clone and version-control changes)

---

## Local Development

### 1. Start Local PostgreSQL

Use the included script **`start-postgres.sh`** which:

- Installs Docker if missing (on Ubuntu/Debian).
- Pulls the `postgres:latest` image.
- Runs a container named `my-postgres`, exposing port `5432`.

```bash
# Make the script executable and run it
chmod +x start-postgres.sh
./start-postgres.sh
```

> **Note**: The script creates a database named `security_standards_db` with username/password `postgres/postgres`. After running, you can connect via `psql -h localhost -p 5432 -U postgres -d security_standards_db`.

Alternatively, if you have your own local DB, set the appropriate `spring.datasource.*` properties in `application.properties`.

### 2. Build the Project

To compile and check everything:

```bash
mvn clean package
```

### 3. Run Liquibase Migrations

By default, the application runs Liquibase at startup (see `application.properties`). You can also manually run:

```bash
mvn liquibase:update
```

This applies **all** pending changesets from `src/main/resources/liquibase/changelog-master.xml` to your local database.

> **Tip**: Use `-Dliquibase.contexts=dev` or `-Dliquibase.contexts=test` to apply environment-specific changes.

### 4. (Optional) Run the Application

Launch the Spring Boot app on your local machine:

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/*-SNAPSHOT.jar
```

The app will start on port 8080 by default. Check logs for a successful connection to PostgreSQL.

---

## Testing

**Integration Tests** use **Testcontainers** (see `BasePostgresContainerTest.java`), which automatically:

- Spins up a **PostgreSQL Docker container** during test initialization.
- Applies Liquibase migrations to that container (via `spring.liquibase.*` properties in `application-test.properties`).
- Runs JUnit test cases against a real Postgres instance.
- Tears down the container after tests.

To run tests:

```bash
mvn clean test
```

You’ll see logs indicating the Postgres container is pulled and started. Tests will verify repository CRUD operations.

---

## Liquibase Changelogs

Under `src/main/resources/liquibase/`:

- **`changelog-master.xml`** – The master entry that includes each changeset in a logical order.
- **`changesets/`** – Separate files for tables, sequences, stored procedures, triggers, etc.
- **`data-inserts/`** – XML files inserting base or test data.

**Common liquibase commands**:

- `mvn liquibase:update` – Apply pending changes.
- `mvn liquibase:status` – Show which changesets have or haven’t been applied.
- `mvn liquibase:rollback` – Roll back changes (if `<rollback>` blocks are defined).

---

## Scripts

### `start-postgres.sh`

- Installs Docker if necessary (Debian/Ubuntu).
- Creates a PostgreSQL container named `my-postgres` with:
  - DB name: `security_standards_db`
  - Username: `postgres`
  - Password: `postgres`
- Maps host port `5432` to container’s `5432`.

### `stop-postgres.sh`

- Stops the running container named `my-postgres` without removing it.

### `remove-postgres.sh`

- Stops **and removes** the container named `my-postgres`.
- Use this if you need a fresh container or to free up resources.

---

## Typical Database Creation Order

Liquibase executes changesets in `changelog-master.xml` from top to bottom:

1. **Sequences** (if needed).
2. **Core tables** (`organizations`, `standards`, `control_requirements`, etc.).
3. **Bridging / reference tables** (e.g., `compliance_mappings`, `organization_standards`).
4. **Views** (like `compliance_summary`).
5. **Stored procedures / triggers**.
6. **Base data** or dev/test data inserts.

---

## Using Profiles and Environments

You can pass contexts or profiles to Maven or Spring Boot:

```bash
# Liquibase context for dev
mvn liquibase:update -Dliquibase.contexts=dev

# Spring Boot with dev profile (if you define "application-dev.properties")
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**`application-test.properties`** is automatically used by JUnit tests annotated with `@ActiveProfiles("test")`.

---

## Rollback and Recovery

1. **Liquibase Rollbacks**: If changesets have `<rollback>` definitions, you can run:
   ```bash
   mvn liquibase:rollback -Dliquibase.rollbackCount=1
   ```
2. **Testcontainers**: Your test DB is ephemeral; no manual rollback is needed—each test run spins up a new container.
3. **Production**: On AWS RDS or other environments, rely on RDS snapshots or carefully tested Liquibase rollback scripts.

---

## Industry-Leading Strategies for Continuous Database Integration

This project adopts multiple **CDI (Continuous Database Integration)** best practices to ensure database changes are versioned, automated, and consistently tested:

1. **Declarative Change Management with Liquibase**

   - All schema changes live in version-controlled **XML files** within `src/main/resources/liquibase/changesets`.
   - Each changeset can be rolled forward or backward, providing a clear audit trail of what changed, when, and by whom.
   - Following [industry recommendations](https://www.liquibase.com/resources/guides/database-continuous-integration) for schema versioning reduces risk and helps maintain compliance in regulated environments.

2. **Automated Integration Testing with Testcontainers**

   - Our CI pipeline spins up an **ephemeral PostgreSQL container** for each test run (see `BasePostgresContainerTest.java`).
   - Liquibase updates are applied automatically before tests execute, validating each database change in an isolated environment.
   - This ensures that application code and DB schema evolve in lockstep, aligning with DevOps “shift-left” testing principles.

3. **Version-Controlled Scripts and Single Source of Truth**

   - The `changelog-master.xml` file references all Liquibase changesets, guaranteeing every environment can synchronize to the same schema state.
   - By treating “database code” (DDL, DML statements) the same as application code, teams achieve **traceability** and **faster feedback**, as recommended by [CDI best practices](https://www.red-gate.com/blog/database-devops/why-standardizing-migrations-across-multiple-database-types-with-flyway-adds-up-for-desjardins).

4. **Incremental, Consistent Deployments**

   - Developers can run `mvn liquibase:update` locally or in CI to apply changes in small increments rather than large, risky “big bang” releases.
   - This incremental approach is aligned with “micro-commit culture” and drastically reduces downtime or disruption when updates move to production.

5. **Reliable Rollbacks and Recovery**

   - Where needed, we use `<rollback>` blocks to revert changesets. In an emergency, we also rely on ephemeral containers or RDS snapshots for major reversions.
   - This dual strategy (Liquibase’s built-in rollback plus environment snapshots) offers confidence during releases, an approach widely recommended in financial and healthcare sectors for mission-critical data.

6. **Security and Compliance**
   - Database credentials for CI testing are managed via environment variables or Docker secrets, never stored in plaintext.
   - Liquibase’s `DATABASECHANGELOG` table provides an audit-friendly record of which changes were deployed, essential for SOX/HIPAA/PCI-DSS compliance.
   - Aligning DB schema changes with application releases ensures we avoid “drift” and maintain consistent auditing.

By combining **Liquibase** for schema versioning, **Testcontainers** for real-database testing, and robust **Maven** pipelines for automation, this project demonstrates the **continuous database integration** approach championed by modern DevOps teams. These practices help avoid downtime, accelerate feature delivery, and ensure data integrity across development, testing, and production environments.

---

## API Documentation (OpenAPI 3.0)

We maintain an **OpenAPI 3.0** specification in [`src/main/resources/openapi/openapi.yaml`](src/main/resources/openapi/openapi.yaml). This file is packaged with the application JAR/WAR so you can:

- **Inspect** it directly from your IDE or text editor.
- **Serve** it via Spring Boot at runtime (e.g., by placing it under `static/` or configuring a resource handler).
- **Use** it to generate API clients or documentation (e.g., with [Swagger Codegen](https://swagger.io/tools/swagger-codegen/) or [OpenAPI Generator](https://openapi-generator.tech/)).

If you place `openapi.yaml` in `src/main/resources/static`, you can access it at `http://localhost:8080/openapi.yaml` once the app is running. Otherwise, configure Spring to serve it from your preferred path.

---

## Contributing

1. **Fork** the repository.
2. **Create** a feature or bugfix branch.
3. **Commit and push** your changes.
4. **Submit** a pull request describing the changes in detail.

We welcome improvements to entities, Liquibase changesets, the OpenAPI spec, or the overall pipeline.

---

## License

Unless otherwise noted, this project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

---

**Thank you** for using the ComplyMatrix Security Standards Database!  
Please [open an issue](https://github.com/your-org/complymatrix/issues) or submit a pull request if you have questions or suggestions.
