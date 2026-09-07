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

**Status:** 🟢 DONE

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

The Angular workspace, PWA configuration, routing, player list/detail, price history and tracking management (status, clause, clause release date, notes) are implemented and covered by unit tests. See `docs/05-frontend.md` and `docs/04-backend.md` (FT-040 to FT-066) for details. The responsive/mobile layout has been polished across the toolbar, player list, player detail and tracking screens, the Docker Compose stack (db + backend + frontend) has been built and verified end-to-end (including a fix to the backend Maven packaging that produced a non-executable jar), and Playwright E2E smoke tests cover the player list, player detail and tracking flows (see `frontend/e2e/`).

---

# Phase 3 — Data Acquisition

**Status:** 🟡 IN PROGRESS

**Objective:** Stop depending on manually entered data.

The application will progressively acquire player and market information from external sources.

### Planned steps

1. Define the external data contract. 🟢 DONE — futbolfantasy.com player market value + trend, see `docs/04-backend.md`.
2. Define player identity matching. 🟢 DONE — `Player.externalId` holds the futbolfantasy.com slug.
3. Implement player synchronisation. ⚪ BACKLOG
4. Implement price synchronisation. 🟡 IN PROGRESS — manual "Actualizar" refresh (single player and bulk) is implemented; scheduled/automatic synchronisation is still pending.
5. Implement trend synchronisation. 🟢 DONE — trend type/amount are scraped and stored alongside price.
6. Store historical observations. 🟢 DONE — reuses the existing `player_price` table/history.
7. Schedule data collection. ⚪ BACKLOG

### Important constraint

Scraping is deliberately deferred until the core application is stable.

The data acquisition layer should be isolated from the rest of the application so that the source or acquisition mechanism can be replaced later.

### Progress so far

A first acquisition source has been implemented: `backend/src/main/java/com/fantasytracker/acquisition/` scrapes the current market value and trend for a player from futbolfantasy.com (`Player.externalId` is the page slug). The player's profile page only shows a loading placeholder for the value widget — the actual data is fetched by the page's own JavaScript from a second "market detail" endpoint keyed by an internal numeric id, so the scraper performs two HTTP requests (profile page to discover the id, then the market detail fragment) and parses the second response with Jsoup.

This is exposed today as a **manual** action:

* `POST /api/players/{playerId}/prices/refresh` — refresh a single player.
* `POST /api/tracking/prices/refresh` — refresh every tracked player in one call, returning a per-player success/failure summary (a scraping failure for one player does not abort the others).

Automatic/scheduled synchronisation (step 7) is intentionally left for later, closer to when the AWS deployment (Phase 5) is tackled, since it is a natural fit for an EventBridge-triggered Lambda or a Spring `@Scheduled` job.

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

Updated after starting Data Acquisition:

```text
Phase 0  ████████████████████  Foundation
Phase 1  ████████████████████  Backend MVP
Phase 2  ████████████████████  Frontend MVP
Phase 3  ██████░░░░░░░░░░░░░  Data Acquisition
Phase 4  ░░░░░░░░░░░░░░░░░░░  Analysis
Phase 5  ░░░░░░░░░░░░░░░░░░░  AWS
Phase 6  ░░░░░░░░░░░░░░░░░░░  Advanced
```

Phase 0, Phase 1 and Phase 2 are complete. Phase 3 (Data Acquisition) is under way: a futbolfantasy.com scraper (market value + trend) is implemented and wired to a manual "Actualizar" refresh, both for a single player and in bulk for the whole watchlist. Scheduled/automatic synchronisation is still pending, likely alongside the AWS deployment work in Phase 5.
