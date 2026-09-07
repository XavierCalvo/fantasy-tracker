# Fantasy Tracker — Backlog

## 1. Purpose

This document contains the concrete development backlog derived from the project roadmap.

The backlog is intentionally more detailed than the roadmap but less detailed than implementation code.

Tasks should be updated as development progresses.

---

## 2. Status Legend

| Status         | Meaning                           |
| -------------- | --------------------------------- |
| ⚪ BACKLOG      | Identified but not started        |
| 🔵 READY       | Defined and ready to implement    |
| 🟡 IN PROGRESS | Currently being implemented       |
| 🟢 DONE        | Completed                         |
| 🔴 BLOCKED     | Blocked by an external dependency |

## 3. Priority

| Priority | Meaning                                   |
| -------- | ----------------------------------------- |
| P0       | Required to make the current phase usable |
| P1       | Important for the current phase           |
| P2       | Valuable but can wait                     |
| P3       | Future / optional                         |

---

# Phase 0 — Project Foundation

| ID     | Status         | Priority | Task                              |
| ------ | -------------- | -------- | --------------------------------- |
| FT-001 | 🟢 DONE        | P0       | Create repository structure       |
| FT-002 | 🟢 DONE        | P0       | Define Git branching strategy     |
| FT-003 | 🟢 DONE        | P0       | Create initial README             |
| FT-004 | 🟢 DONE        | P0       | Configure `.gitignore`            |
| FT-005 | 🟢 DONE        | P0       | Create Docker Compose             |
| FT-006 | 🟢 DONE        | P0       | Configure local PostgreSQL        |
| FT-007 | 🟢 DONE        | P0       | Create Spring Boot backend        |
| FT-008 | 🟢 DONE        | P0       | Configure Java 21                 |
| FT-009 | 🟢 DONE        | P0       | Integrate Flyway                  |
| FT-010 | 🟢 DONE        | P0       | Define initial database schema    |
| FT-011 | 🟢 DONE        | P0       | Implement Player entity           |
| FT-012 | 🟢 DONE        | P0       | Implement PlayerPrice entity      |
| FT-013 | 🟢 DONE        | P0       | Implement TrackedPlayer entity    |
| FT-014 | 🟢 DONE        | P0       | Implement player price trend enum |
| FT-015 | 🟢 DONE        | P0       | Implement tracking status enum    |
| FT-016 | 🟢 DONE        | P1       | Create backend Dockerfile         |
| FT-017 | 🟢 DONE        | P0       | Validate GitHub Actions CI        |
| FT-018 | 🟢 DONE        | P0       | Add backend automated tests       |
| FT-019 | 🟢 DONE        | P1       | Complete project documentation    |

### Phase 0 acceptance criteria

* Local environment can be started reproducibly.
* PostgreSQL is available through Docker.
* Backend connects successfully to PostgreSQL.
* Flyway creates the schema.
* Domain entities correspond to the database model.
* CI executes successfully.
* Core domain behaviour has automated test coverage.

---

# Phase 1 — Backend MVP

## API

| ID     | Status         | Priority | Task                            |
| ------ | -------------- | -------- | ------------------------------- |
| FT-020 | 🟢 DONE        | P0       | Create Player repository        |
| FT-021 | 🟢 DONE        | P0       | Create PlayerPrice repository   |
| FT-022 | 🟢 DONE        | P0       | Create TrackedPlayer repository |
| FT-023 | 🟢 DONE        | P0       | Complete Player REST API        |
| FT-024 | 🟢 DONE        | P0       | Create PlayerPrice REST API     |
| FT-025 | 🟢 DONE        | P0       | Create TrackedPlayer REST API   |
| FT-026 | 🟢 DONE        | P1       | Add request/response DTOs       |
| FT-027 | 🟢 DONE        | P1       | Add bean/domain validation      |
| FT-028 | 🟢 DONE        | P1       | Add global API error handling   |
| FT-029 | 🟢 DONE        | P1       | Define API response conventions |

## Backend testing

| ID     | Status    | Priority | Task                            |
| ------ | --------- | -------- | ------------------------------- |
| FT-030 | 🟢 DONE   | P0       | Test Player persistence         |
| FT-031 | 🟢 DONE   | P0       | Test Player API                 |
| FT-032 | 🟢 DONE   | P0       | Test PlayerPrice persistence    |
| FT-033 | 🟢 DONE   | P0       | Test PlayerPrice API            |
| FT-034 | 🟢 DONE   | P0       | Test TrackedPlayer persistence  |
| FT-035 | 🟢 DONE   | P0       | Test TrackedPlayer API          |
| FT-036 | 🟢 DONE   | P1       | Test validation/error scenarios |

### Phase 1 acceptance criteria

The backend must expose enough functionality for the frontend to perform the complete MVP user flow without direct database access.

---

# Phase 2 — Frontend MVP

## Application foundation

| ID     | Status    | Priority | Task                            |
| ------ | --------- | -------- | ------------------------------- |
| FT-040 | 🟢 DONE   | P0       | Create Angular application      |
| FT-041 | 🟢 DONE   | P0       | Configure TypeScript            |
| FT-042 | 🟢 DONE   | P0       | Configure Angular routing       |
| FT-043 | 🟢 DONE   | P0       | Configure PWA                   |
| FT-044 | 🟡 IN PROGRESS | P1  | Define responsive/mobile layout |
| FT-045 | 🟢 DONE   | P1       | Define common UI components     |

## Player experience

| ID     | Status    | Priority | Task                         |
| ------ | --------- | -------- | ----------------------------- |
| FT-050 | 🟢 DONE   | P0       | Create player API service    |
| FT-051 | 🟢 DONE   | P0       | Create player list           |
| FT-052 | 🟢 DONE   | P0       | Create player detail         |
| FT-053 | 🟢 DONE   | P0       | Display current price        |
| FT-054 | 🟢 DONE   | P0       | Display current trend        |
| FT-055 | 🟢 DONE   | P1       | Display price history        |
| FT-056 | 🟢 DONE   | P1       | Display player team/position |
| FT-057 | 🟢 DONE   | P0       | Create player (form + `/players/new` route) |
| FT-058 | 🟢 DONE   | P0       | Edit player (form + `/players/:id/edit` route, to fix team/position/external id changes or creation mistakes) |

## Tracking experience

| ID     | Status    | Priority | Task                        |
| ------ | --------- | -------- | ---------------------------- |
| FT-060 | 🟢 DONE   | P0       | Create tracking API service |
| FT-061 | 🟢 DONE   | P0       | Display tracking status     |
| FT-062 | 🟢 DONE   | P0       | Update tracking status      |
| FT-063 | 🟢 DONE   | P0       | Store release clause        |
| FT-064 | 🟢 DONE   | P1       | Store clause release date   |
| FT-065 | 🟢 DONE   | P1       | Store player notes          |
| FT-066 | 🟢 DONE   | P1       | Create tracked-player view  |
| FT-067 | 🟢 DONE   | P1       | Add `GET /api/tracking` backend endpoint (list all tracked players enriched with player name/team/position, used by the watchlist screen) |
| FT-068 | ⚪ BACKLOG | P1       | Turn `Player.position` into a fixed `PlayerPosition` enum (`PORTERO`, `DEFENSA`, `MEDIO`, `DELANTERO`, `ENTRENADOR`) instead of free text; edit `V1__init.sql` directly (no migration needed, table is still empty); update `PlayerRequest`/`PlayerResponse` accordingly |
| FT-069 | ⚪ BACKLOG | P1       | Replace the free-text position input in `PlayerForm` with a `mat-select` offering the fixed options in order (Portero, Defensa, Medio, Delantero, Entrenador), defaulting to Defensa when creating a new player |

### Phase 2 acceptance criteria

A complete player can be viewed and managed from a mobile browser, including market information and personal tracking information.

All acceptance criteria are met for the core flow: `PlayerApi`/`PlayerPriceApi`/`TrackedPlayerApi` cover the REST contract, `PlayerList`/`PlayerDetail` render players, price history and a tracking form (status, clause, clause release date, notes), and a shared `StatusMessage` component provides consistent loading/error/empty states. Unit tests (Vitest) cover all services and components. FT-066 is implemented as a dedicated **watchlist screen** (`TrackedPlayers`, route `/tracked`) that lists every tracked player, filterable by status and sortable by clause release date, name or status; it is now the app's default landing route, since it is more actionable to the user than a bare player list. This required a small backend addition (FT-067: `GET /api/tracking`, returning tracked players joined with player name/team/position) since no endpoint previously existed to list tracked players across all players. FT-057/FT-058 add a shared `PlayerForm` component (create at `/players/new`, edit at `/players/:id/edit`) so players can be registered and corrected (team, position, Futbolfantasy external id) entirely from the UI — the backend `POST`/`PUT /api/players` endpoints already existed from Phase 1 but had no frontend view. Remaining polish items: richer mobile navigation and an end-to-end verification of the Docker Compose stack.

---

# Phase 3 — Data Acquisition

| ID     | Status    | Priority | Task                                 |
| ------ | --------- | -------- | ------------------------------------ |
| FT-070 | ⚪ BACKLOG | P0       | Define external player data contract |
| FT-071 | ⚪ BACKLOG | P0       | Define external player ID mapping    |
| FT-072 | ⚪ BACKLOG | P0       | Implement player synchronisation     |
| FT-073 | ⚪ BACKLOG | P0       | Implement price synchronisation      |
| FT-074 | ⚪ BACKLOG | P1       | Implement trend synchronisation      |
| FT-075 | ⚪ BACKLOG | P1       | Validate imported data               |
| FT-076 | ⚪ BACKLOG | P1       | Prevent duplicate price observations |
| FT-077 | ⚪ BACKLOG | P1       | Isolate external-source integration  |
| FT-078 | ⚪ BACKLOG | P1       | Implement scheduled acquisition      |

Scraping should only be implemented after the external data contract and acquisition boundary are clearly defined.

---

# Phase 4 — Analysis

| ID     | Status    | Priority | Task                              |
| ------ | --------- | -------- | --------------------------------- |
| FT-080 | ⚪ BACKLOG | P1       | Calculate 24h price change        |
| FT-081 | ⚪ BACKLOG | P1       | Calculate 3-day price change      |
| FT-082 | ⚪ BACKLOG | P1       | Calculate 7-day price change      |
| FT-083 | ⚪ BACKLOG | P1       | Calculate price velocity          |
| FT-084 | ⚪ BACKLOG | P1       | Calculate price acceleration      |
| FT-085 | ⚪ BACKLOG | P1       | Analyse trend evolution           |
| FT-086 | ⚪ BACKLOG | P1       | Analyse clause/value relationship |
| FT-087 | ⚪ BACKLOG | P1       | Create player valuation           |
| FT-088 | ⚪ BACKLOG | P1       | Create opportunity ranking        |
| FT-089 | ⚪ BACKLOG | P2       | Create watchlist prioritisation   |
| FT-090 | ⚪ BACKLOG | P2       | Create squad recommendations      |

---

# Phase 5 — AWS

| ID     | Status    | Priority | Task                             |
| ------ | --------- | -------- | -------------------------------- |
| FT-100 | ⚪ BACKLOG | P2       | Define AWS architecture          |
| FT-101 | ⚪ BACKLOG | P2       | Create RDS PostgreSQL            |
| FT-102 | ⚪ BACKLOG | P2       | Configure production Flyway      |
| FT-103 | ⚪ BACKLOG | P2       | Deploy backend                   |
| FT-104 | ⚪ BACKLOG | P2       | Configure API Gateway            |
| FT-105 | ⚪ BACKLOG | P2       | Deploy frontend with Amplify     |
| FT-106 | ⚪ BACKLOG | P2       | Configure EventBridge scheduling |
| FT-107 | ⚪ BACKLOG | P2       | Configure production secrets     |
| FT-108 | ⚪ BACKLOG | P2       | Configure monitoring/logging     |
| FT-109 | ⚪ BACKLOG | P2       | Document deployment procedure    |

---

# Phase 6 — Advanced Features

| ID     | Status    | Priority | Task                                          |
| ------ | --------- | -------- | --------------------------------------------- |
| FT-120 | ⚪ BACKLOG | P3       | Advanced player filtering                     |
| FT-121 | ⚪ BACKLOG | P3       | Custom rankings                               |
| FT-122 | ⚪ BACKLOG | P3       | Opportunity alerts                            |
| FT-123 | ⚪ BACKLOG | P3       | Historical market analysis                    |
| FT-124 | ⚪ BACKLOG | P3       | Squad simulation                              |
| FT-125 | ⚪ BACKLOG | P3       | Advanced visualisations                       |
| FT-126 | ⚪ BACKLOG | P3       | Performance optimisation                      |
| FT-127 | ⚪ BACKLOG | P3       | Support additional fantasy competitions       |
| FT-128 | ⚪ BACKLOG | P3       | Authentication/multi-user support if required |

---

# 4. Immediate Next Tasks

The recommended execution order from the current state is:

1. **FT-044 — Finish responsive/mobile layout polish**
2. Verify the Docker Compose stack end-to-end (backend + frontend + db)
3. Add frontend E2E tests for the core player/tracking journey
4. **FT-070 — Define external player data contract** (Phase 3 kickoff, once Phase 2 is closed)

Phase 2 (Frontend MVP) core functionality is implemented; remaining work is UX polish and end-to-end verification before moving to Phase 3 (Data Acquisition).

---

# 5. Backlog Rules

The backlog should remain the single detailed view of implementation work.

When adding a task:

* Give it a unique `FT-XXX` identifier.
* Assign a phase.
* Assign a priority.
* Define a clear outcome.
* Keep implementation details out unless they are important to the acceptance criteria.

When completing a task:

* Change its status to `🟢 DONE`.
* Do not remove it from the backlog.
* Update the roadmap if the phase status changes.

When a significant architectural or product decision is made, document the decision separately rather than embedding a long explanation in the backlog.
