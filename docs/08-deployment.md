# Fantasy Tracker — Deployment

## 1. Purpose

This document describes how Fantasy Tracker is expected to run locally and how the application will eventually be deployed to AWS.

The deployment strategy is divided into:

* Local development.
* CI.
* Future production environment.

---

# 2. Local Environment

The current local backend environment uses Docker Compose.

```text
┌───────────────────────────────┐
│        Docker Compose         │
│                               │
│  ┌─────────────┐              │
│  │ PostgreSQL  │              │
│  │    :5432    │              │
│  └──────┬──────┘              │
│         │                     │
│  ┌──────▼──────┐              │
│  │   Backend   │              │
│  │    :8080    │              │
│  └─────────────┘              │
│                               │
└───────────────────────────────┘
```

The current Compose file defines:

* PostgreSQL 15.
* Spring Boot backend.
* Persistent PostgreSQL volume.
* Health check for PostgreSQL.
* Internal Docker network.

---

# 3. Local Database

Default local configuration:

```text
Database: fantasy
User: fantasy
Password: fantasy
Host: db
Port: 5432
```

These values are development defaults only.

Production credentials must never be committed to the repository.

---

# 4. Database Migrations

Flyway executes database migrations when the backend starts.

Current migration:

```text
backend/src/main/resources/db/migration/V1__init.sql
```

The production database must always be created and updated through Flyway.

Manual schema modifications should not be used as a normal deployment mechanism.

---

# 5. Backend Container

The backend uses a multi-stage Docker build.

```text
Maven build image
       ↓
Compile/package
       ↓
JRE runtime image
       ↓
Spring Boot JAR
```

This keeps build dependencies out of the runtime image.

The container exposes port `8080`.

---

# 6. Frontend Deployment

During local development the Angular application runs independently from the backend via `ng serve`, using a dev-server proxy (`proxy.conf.json`) so `/api` requests are forwarded to `http://localhost:8080` without CORS issues.

The frontend also has a multi-stage Docker build (`frontend/Dockerfile`): a Node build stage produces the production bundle, which is then served by nginx (`frontend/nginx.conf`). The nginx config proxies `/api/*` to the `backend` container on the Docker network and falls back to `index.html` for Angular's client-side routing. This container is wired into `docker-compose.yml` as the `frontend` service (port `4200` → `80`).

For production, the target is to serve the Angular PWA through AWS Amplify Hosting or an equivalent static hosting solution.

Target flow:

```text
User
 ↓
Angular PWA
 ↓
API Gateway
 ↓
Backend
```

---

# 7. CI

GitHub Actions is the CI platform.

The CI pipeline should validate the application before changes are considered ready for integration.

Target pipeline:

```text
Push / Pull Request
        ↓
Checkout
        ↓
Backend build & tests
        ↓
Frontend build & tests
        ↓
Quality checks
        ↓
Container build
```

Deployment to production should only be introduced once the application has a stable MVP.

---

# 8. AWS Target Architecture

The initial target AWS architecture is:

```text
                         ┌─────────────────────┐
                         │    AWS Amplify      │
                         │     Frontend        │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │    API Gateway      │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │       Lambda        │
                         │   Spring Boot API   │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │     RDS PostgreSQL  │
                         └─────────────────────┘

                         ┌─────────────────────┐
                         │     EventBridge     │
                         │   Scheduled jobs    │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         Data acquisition layer
```

This is the target architecture, not the current production architecture.

---

# 9. AWS Components

## Amplify

Expected responsibility:

* Frontend hosting.
* Static asset delivery.
* Frontend deployment automation.

## API Gateway

Expected responsibility:

* Public API entry point.
* Routing requests to backend functions.
* Future API-level controls.

## Lambda

Expected responsibility:

* Backend execution in the serverless target architecture.

The exact approach to running Spring Boot on Lambda will be evaluated before implementation.

A containerised Spring Boot service may be preferred if the operational characteristics make it more appropriate.

## RDS PostgreSQL

Expected responsibility:

* Production relational database.
* Persistent player and market history.

## EventBridge

Expected responsibility:

* Scheduled data acquisition.
* Future periodic processing jobs.

---

# 10. Environments

The expected environments are:

```text
Local
  ↓
CI
  ↓
Production
```

A dedicated staging environment may be introduced if the application becomes sufficiently complex to justify it.

---

# 11. Configuration and Secrets

Configuration must be externalised.

Examples:

* Database URL.
* Database credentials.
* External data-source credentials if ever required.
* AWS configuration.
* API configuration.

Secrets must not be stored in:

* Source code.
* Git history.
* Docker images.
* Public configuration files.

---

# 12. Monitoring

Production monitoring will be introduced together with the AWS deployment.

Expected areas:

* Application errors.
* API availability.
* Lambda failures, if Lambda is used.
* Database availability.
* Scheduled acquisition failures.
* Data processing failures.

The exact monitoring stack is TBD.

---

# 13. Current Status

| Area                   | Status            |
| ---------------------- | ----------------- |
| Local PostgreSQL       | 🟢 DONE           |
| Local Docker Compose   | 🟢 DONE           |
| Backend Docker image   | 🟢 DONE           |
| Flyway local execution | 🟢 DONE           |
| Backend CI (build & test) | 🟢 DONE        |
| Frontend Docker image  | 🟢 DONE           |
| Frontend CI            | 🟢 DONE           |
| Frontend deployment (local, Docker/nginx) | 🟢 DONE |
| Frontend deployment (AWS Amplify)         | ⚪ BACKLOG |
| AWS architecture       | 🔵 TARGET DEFINED |
| RDS                    | ⚪ BACKLOG         |
| API Gateway            | ⚪ BACKLOG         |
| Lambda                 | ⚪ BACKLOG         |
| Amplify                | ⚪ BACKLOG         |
| EventBridge            | ⚪ BACKLOG         |
| Production monitoring  | ⚪ BACKLOG         |

The AWS architecture should not be treated as final until the MVP and data acquisition requirements are better understood.
