# Fantasy Tracker — Roadmap

## 1. Purpose

This document defines the major development phases of Fantasy Tracker and the expected evolution from the current local MVP foundation to a complete cloud-based fantasy decision-support tool.

The roadmap is intentionally incremental.

The objective is to reach a useful product early rather than waiting until every future feature is implemented.

---

## 2. Status Legend

| Status         | Meaning                                           |
| -------------- | ------------------------------------------------- |
| ⚪ BACKLOG      | Identified but not started                        |
| 🔵 READY       | Clearly defined and ready to implement            |
| 🟡 IN PROGRESS | Currently being implemented                       |
| 🟢 DONE        | Completed and integrated                          |
| 🔴 BLOCKED     | Cannot progress because of an external dependency |

---

# Phase 0 — Project Foundation

**Objective:** Establish a stable technical foundation for the application.

### Status

| Task                          | Status         |
| ----------------------------- | -------------- |
| Repository structure          | 🟢 DONE        |
| Git branching strategy        | 🟢 DONE        |
| README                        | 🟢 DONE        |
| Docker Compose                | 🟢 DONE        |
| PostgreSQL local environment  | 🟢 DONE        |
| Spring Boot backend           | 🟢 DONE        |
| Java 21 baseline               | 🟢 DONE        |
| Flyway integration            | 🟢 DONE        |
| Initial database model        | 🟢 DONE        |
| Player entity                 | 🟢 DONE        |
| PlayerPrice entity            | 🟢 DONE        |
| TrackedPlayer entity          | 🟢 DONE        |
| Trend type model              | 🟢 DONE        |
| Backend Dockerfile            | 🟢 DONE        |
| GitHub Actions CI             | 🟢 DONE        |
| Backend automated tests       | 🟢 DONE        |
| Initial project documentation | 🟢 DONE        |

### Exit criteria

Phase 0 is complete when:

* The repository has a reproducible local environment.
* PostgreSQL starts through Docker Compose.
* The backend starts against PostgreSQL.
* Flyway manages the initial schema.
* CI validates the backend.
* The core domain model is covered by tests.
* Project documentation reflects the actual architecture.

---

# Phase 1 — Backend MVP

**Status:** 🟢 DONE

**Objective:** Provide a usable REST API for the complete initial domain.

### Main areas

* Player API.
* Player price API.
* Tracking API.
* DTOs.
* Validation.
* Error handling.
* API tests.

### Target capabilities

The API should allow the frontend to:

* List players.
* Retrieve a player.
* Create/update player information.
* Retrieve price history.
* Record a new price observation.
* Retrieve tracking information.
* Create/update/delete tracking information.

### Exit criteria

A complete API exists for the MVP domain and is covered by automated tests.

All exit criteria are met: Player/PlayerPrice/TrackedPlayer REST endpoints exist, requests/responses use DTOs, bean validation is applied, errors are handled through a global `ApiError` response, and MockMvc tests cover the CRUD, not-found and validation scenarios for all three resources. See `docs/04-backend.md` (FT-020 to FT-036) and `docs/02-architecture.md` §5 for the resulting API conventions.

---

# Phase 2 — Frontend MVP

**Status:** 🟡 IN PROGRESS

**Objective:** Build the first usable mobile application.

### Main areas

* Angular application.
* PWA configuration.
* Routing.
* Backend API integration.
* Player list.
* Player detail.
* Price history.
* Tracking management.
* Clause information.
* Notes.
* Mobile navigation.

### Target user flow

```text
Open application
      ↓
Player list
      ↓
Select player
      ↓
Player detail
      ├── Current price
      ├── Trend
      ├── Price history
      └── Personal tracking
             ├── Status
             ├── Clause
             └── Notes
```

### Exit criteria

The complete MVP can be used from a mobile browser without direct access to the backend or database.

The Angular workspace, PWA configuration, routing, player list/detail, price history and tracking management (status, clause, clause release date, notes) are implemented and covered by unit tests. See `docs/05-frontend.md` and `docs/04-backend.md` (FT-040 to FT-066) for details. Remaining work: polish the responsive/mobile layout further, verify the Docker Compose stack end-to-end, and add frontend E2E tests.

---

# Phase 3 — Data Acquisition

**Objective:** Stop depending on manually entered data.

The application will progressively acquire player and market information from external sources.

### Planned steps

1. Define the external data contract.
2. Define player identity matching.
3. Implement player synchronisation.
4. Implement price synchronisation.
5. Implement trend synchronisation.
6. Store historical observations.
7. Schedule data collection.

### Important constraint

Scraping is deliberately deferred until the core application is stable.

The data acquisition layer should be isolated from the rest of the application so that the source or acquisition mechanism can be replaced later.

### Exit criteria

The database is populated automatically with reliable player and price history.

---

# Phase 4 — Analysis & Decision Support

**Objective:** Transform historical data into useful fantasy decisions.

### Planned capabilities

* Price evolution.
* 24-hour price change.
* 3-day price change.
* 7-day price change.
* Price velocity.
* Price acceleration.
* Trend analysis.
* Clause analysis.
* Player valuation.
* Opportunity ranking.
* Watchlist prioritisation.
* Squad improvement recommendations.

### Example future questions

```text
Which players are accelerating upwards?

Which watched players have the best value?

Which players have increased significantly
but still have an attractive clause?

Which players should be sold?

Which position should be reinforced?
```

### Exit criteria

The application provides actionable insights rather than only raw player data.

---

# Phase 5 — AWS Deployment

**Objective:** Make the application continuously available without requiring a local environment.

### Planned infrastructure

* AWS Amplify Hosting.
* API Gateway.
* Lambda.
* Amazon RDS PostgreSQL.
* EventBridge.
* Appropriate monitoring/logging.

### Main tasks

* Define AWS infrastructure.
* Create production database.
* Configure migrations.
* Deploy backend.
* Deploy frontend.
* Configure scheduled data acquisition.
* Configure environment variables/secrets.
* Add monitoring.
* Document operational procedures.

### Exit criteria

The application is available remotely and can acquire and process data automatically.

---

# Phase 6 — Optimisation & Advanced Features

**Objective:** Improve usability and decision quality once the core product is stable.

Potential future work:

* Advanced player filtering.
* Custom rankings.
* Notifications.
* Opportunity alerts.
* Historical market analysis.
* Squad simulation.
* Better visualisations.
* Performance optimisation.
* Additional fantasy competitions.
* User accounts and multi-user support, if ever required.

This phase is intentionally open-ended and should only be prioritised once the previous phases provide enough reliable data and value.

---

# Current Position

Updated after starting the Frontend MVP:

```text
Phase 0  ████████████████████  Foundation
Phase 1  ████████████████████  Backend MVP
Phase 2  ███████████████░░░░  Frontend MVP
Phase 3  ░░░░░░░░░░░░░░░░░░░  Data Acquisition
Phase 4  ░░░░░░░░░░░░░░░░░░░  Analysis
Phase 5  ░░░░░░░░░░░░░░░░░░░  AWS
Phase 6  ░░░░░░░░░░░░░░░░░░░  Advanced
```

Phase 0 and Phase 1 are complete. Phase 2 (Frontend MVP) is now in progress: the Angular application, player list/detail and tracking management are implemented; remaining work covers mobile UX polish, an end-to-end Docker Compose verification and E2E tests.
