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
| FT-068 | 🟢 DONE   | P1       | Turn `Player.position` into a fixed `PlayerPosition` enum (`PORTERO`, `DEFENSA`, `MEDIO`, `DELANTERO`, `ENTRENADOR`) instead of free text; edit `V1__init.sql` directly (no migration needed, table is still empty); update `PlayerRequest`/`PlayerResponse` accordingly |
| FT-069 | 🟢 DONE   | P1       | Replace the free-text position input in `PlayerForm` with a `mat-select` offering the fixed options in order (Portero, Defensa, Medio, Delantero, Entrenador), defaulting to Defensa when creating a new player |
| FT-070 | 🟢 DONE   | P2       | Create minimal `Team` entity (id, name only) + repository; edit `V1__init.sql` directly (no migration needed, table is still empty); change `Player.team` from free text to a `teamId` reference and update `PlayerRequest`/`PlayerResponse` accordingly |
| FT-071 | 🟢 DONE   | P2       | Create minimal `Team` REST API (list/get/create/update — no delete needed yet) |
| FT-072 | 🟢 DONE   | P2       | Create a minimal Team maintenance screen (list + create/edit form) in the frontend; place its nav entry low-visibility since it is only expected to be used once per season (e.g. new season's teams) |
| FT-073 | 🟢 DONE   | P2       | Replace the free-text team input in `PlayerForm` with a `mat-select` loading teams from the Team API (no on-the-fly team creation; teams must already exist from the maintenance screen) |
| FT-074 | 🟢 DONE   | P2       | Document each REST endpoint in Swagger/OpenAPI (`@Tag` per controller, `@Operation` summary/description per method) so it's clear from the UI whether an endpoint searches, creates, updates or refreshes a price |

### Phase 2 acceptance criteria

A complete player can be viewed and managed from a mobile browser, including market information and personal tracking information.

All acceptance criteria are met for the core flow: `PlayerApi`/`PlayerPriceApi`/`TrackedPlayerApi` cover the REST contract, `PlayerList`/`PlayerDetail` render players, price history and a tracking form (status, clause, clause release date, notes), and a shared `StatusMessage` component provides consistent loading/error/empty states. Unit tests (Vitest) cover all services and components. FT-066 is implemented as a dedicated **watchlist screen** (`TrackedPlayers`, route `/tracked`) that lists every tracked player, filterable by status and sortable by clause release date, name or status; it is now the app's default landing route, since it is more actionable to the user than a bare player list. This required a small backend addition (FT-067: `GET /api/tracking`, returning tracked players joined with player name/team/position) since no endpoint previously existed to list tracked players across all players. FT-057/FT-058 add a shared `PlayerForm` component (create at `/players/new`, edit at `/players/:id/edit`) so players can be registered and corrected (team, position, Futbolfantasy external id) entirely from the UI — the backend `POST`/`PUT /api/players` endpoints already existed from Phase 1 but had no frontend view. Remaining polish items: richer mobile navigation and an end-to-end verification of the Docker Compose stack.

FT-068/FT-069 replace the free-text `position` field with a fixed `PlayerPosition` enum (`PORTERO`, `DEFENSA`, `MEDIO`, `DELANTERO`, `ENTRENADOR`), edited directly into `V1__init.sql` since the table was still empty; the frontend now shows a `mat-select` in that order, defaulting to Defensa on creation. FT-070/FT-071/FT-072/FT-073 introduce a minimal `Team` catalog (id + name), replacing the free-text `team` field with a `teamId` reference (`PlayerResponse` now also exposes the denormalized `teamName` for display); a low-visibility "Mantenimiento de equipos" screen (route `/teams`, linked from a small toolbar icon) allows creating/renaming teams, and `PlayerForm` loads teams from `GET /api/teams` into a `mat-select` instead of free text (no on-the-fly team creation).

FT-074 adds a `@Tag` per controller (Players, Teams, Tracking, Player Prices) and an `@Operation` summary/description per endpoint, so `/swagger-ui` clearly states each endpoint's intent (e.g. "Crear jugador" vs. "Actualizar jugador" vs. "Actualizar precio (Actualizar)" for the market-price refresh action), with no change to request/response contracts.

---

# Phase 3 — Data Acquisition

| ID     | Status    | Priority | Task                                 |
| ------ | --------- | -------- | ------------------------------------ |
| FT-075 | 🟢 DONE   | P0       | Define external player data contract (futbolfantasy.com market value + trend, scraped via a two-step fetch: profile page → numeric market id → market-detail fragment) |
| FT-076 | 🟢 DONE   | P0       | Define external player ID mapping (`Player.externalId` holds the futbolfantasy.com page slug) |
| FT-077 | ⚪ BACKLOG | P0       | Implement player synchronisation     |
| FT-078 | 🟡 IN PROGRESS | P0  | Implement price synchronisation — manual refresh implemented (`POST /api/players/{id}/prices/refresh` for one player, `POST /api/tracking/prices/refresh` for every tracked player at once); scheduled/automatic synchronisation still pending |
| FT-079 | 🟢 DONE   | P1       | Implement trend synchronisation (trend type/amount scraped and stored alongside price) |
| FT-080 | ⚪ BACKLOG | P1       | Validate imported data               |
| FT-081 | ⚪ BACKLOG | P1       | Prevent duplicate price observations |
| FT-082 | 🟢 DONE   | P1       | Isolate external-source integration (`com.fantasytracker.acquisition` package: `PlayerMarketDataScraper` interface, `FutbolFantasyPlayerScraper`/`FutbolFantasyPriceParser` implementation, `PlayerMarketDataException`) |
| FT-083 | ⚪ BACKLOG | P1       | Implement scheduled acquisition      |
| FT-084 | 🟢 DONE   | P1       | Bulk refresh endpoint (`POST /api/tracking/prices/refresh`) and "Actualizar todos" button on the watchlist screen; per-player scraping failures are reported individually and do not abort the batch; discarded players are skipped (a manual per-player refresh is still always available for them) |

Scraping should only be implemented after the external data contract and acquisition boundary are clearly defined.

### Notes

* FT-075/FT-076/FT-082: the player's profile page (`https://www.futbolfantasy.com/jugadores/<externalId>`) does not itself contain the market value widget — it is loaded client-side via AJAX from a second endpoint keyed by an internal numeric id embedded in the page's inline script. `FutbolFantasyPlayerScraper` performs both requests; `FutbolFantasyPriceParser` parses the second response with Jsoup.
* FT-078/FT-084: both the single-player and bulk refresh endpoints reuse the same `PlayerMarketDataScraper`; a missing/unparseable market value raises `PlayerMarketDataException`, which is surfaced as a `502 Bad Gateway` with a descriptive message for the single-player endpoint, or recorded per-player in the bulk endpoint's response so the rest of the batch still completes.
* `GET /api/tracking` was enriched to also return each tracked player's most recent price/trend/capture date, so the watchlist can show it without an extra request per player; the frontend flags any price older than 24h with a clock icon (both in the watchlist and in the player detail screen).

---

# Phase 4 — Analysis

| ID     | Status    | Priority | Task                              |
| ------ | --------- | -------- | --------------------------------- |
| FT-085 | ⚪ BACKLOG | P1       | Calculate 24h price change        |
| FT-086 | ⚪ BACKLOG | P1       | Calculate 3-day price change      |
| FT-087 | ⚪ BACKLOG | P1       | Calculate 7-day price change      |
| FT-088 | ⚪ BACKLOG | P1       | Calculate price velocity          |
| FT-089 | ⚪ BACKLOG | P1       | Calculate price acceleration      |
| FT-090 | ⚪ BACKLOG | P1       | Analyse trend evolution           |
| FT-091 | ⚪ BACKLOG | P1       | Analyse clause/value relationship |
| FT-092 | ⚪ BACKLOG | P1       | Create player valuation           |
| FT-093 | ⚪ BACKLOG | P1       | Create opportunity ranking        |
| FT-094 | ⚪ BACKLOG | P2       | Create watchlist prioritisation   |
| FT-095 | ⚪ BACKLOG | P2       | Create squad recommendations      |

---

# Phase 5 — AWS

| ID     | Status    | Priority | Task                             |
| ------ | --------- | -------- | -------------------------------- |
| FT-105 | ⚪ BACKLOG | P2       | Define AWS architecture          |
| FT-106 | ⚪ BACKLOG | P2       | Create RDS PostgreSQL            |
| FT-107 | ⚪ BACKLOG | P2       | Configure production Flyway      |
| FT-108 | ⚪ BACKLOG | P2       | Deploy backend                   |
| FT-109 | ⚪ BACKLOG | P2       | Configure API Gateway            |
| FT-110 | ⚪ BACKLOG | P2       | Deploy frontend with Amplify     |
| FT-111 | ⚪ BACKLOG | P2       | Configure EventBridge scheduling |
| FT-112 | ⚪ BACKLOG | P2       | Configure production secrets     |
| FT-113 | ⚪ BACKLOG | P2       | Configure monitoring/logging     |
| FT-114 | ⚪ BACKLOG | P2       | Document deployment procedure    |

---

# Phase 6 — Advanced Features

| ID     | Status    | Priority | Task                                          |
| ------ | --------- | -------- | --------------------------------------------- |
| FT-125 | ⚪ BACKLOG | P3       | Advanced player filtering                     |
| FT-126 | ⚪ BACKLOG | P3       | Custom rankings                               |
| FT-127 | ⚪ BACKLOG | P3       | Opportunity alerts                            |
| FT-128 | ⚪ BACKLOG | P3       | Historical market analysis                    |
| FT-129 | ⚪ BACKLOG | P3       | Squad simulation                              |
| FT-130 | ⚪ BACKLOG | P3       | Advanced visualisations                       |
| FT-131 | ⚪ BACKLOG | P3       | Performance optimisation                      |
| FT-132 | ⚪ BACKLOG | P3       | Support additional fantasy competitions       |
| FT-133 | ⚪ BACKLOG | P3       | Authentication/multi-user support if required |

---

# 4. Immediate Next Tasks

The recommended execution order from the current state is:

1. **FT-080/FT-081 — Validate imported data / prevent duplicate price observations** (basic guardrails around the scraper before relying on it more heavily)
2. **FT-077 — Implement player synchronisation** (currently players are still created manually; needed before scheduled acquisition makes sense)
3. **FT-083 — Implement scheduled acquisition** (turn the manual "Actualizar"/"Actualizar todos" buttons into an automatic job, likely tied to the AWS deployment in Phase 5, e.g. an EventBridge-triggered Lambda or a Spring `@Scheduled` job)
4. Once Phase 3's core loop is reliable, move to Phase 4 (Analysis) or Phase 5 (AWS) depending on priorities

Phase 2 (Frontend MVP) is complete. Phase 3 (Data Acquisition) is under way: the futbolfantasy.com scraper (FT-075/FT-076/FT-079/FT-082), manual single/bulk price refresh (FT-078/FT-084) and watchlist trend/staleness display are implemented; player synchronisation and scheduled acquisition remain.

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
