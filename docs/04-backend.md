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
| FT-017 | 🟡 IN PROGRESS | P0       | Validate GitHub Actions CI        |
| FT-018 | ⚪ BACKLOG      | P0       | Add backend automated tests       |
| FT-019 | 🟡 IN PROGRESS | P1       | Complete project documentation    |

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
| FT-023 | 🟡 IN PROGRESS | P0       | Complete Player REST API        |
| FT-024 | ⚪ BACKLOG      | P0       | Create PlayerPrice REST API     |
| FT-025 | ⚪ BACKLOG      | P0       | Create TrackedPlayer REST API   |
| FT-026 | ⚪ BACKLOG      | P1       | Add request/response DTOs       |
| FT-027 | ⚪ BACKLOG      | P1       | Add bean/domain validation      |
| FT-028 | ⚪ BACKLOG      | P1       | Add global API error handling   |
| FT-029 | ⚪ BACKLOG      | P1       | Define API response conventions |

## Backend testing

| ID     | Status    | Priority | Task                            |
| ------ | --------- | -------- | ------------------------------- |
| FT-030 | ⚪ BACKLOG | P0       | Test Player persistence         |
| FT-031 | ⚪ BACKLOG | P0       | Test Player API                 |
| FT-032 | ⚪ BACKLOG | P0       | Test PlayerPrice persistence    |
| FT-033 | ⚪ BACKLOG | P0       | Test PlayerPrice API            |
| FT-034 | ⚪ BACKLOG | P0       | Test TrackedPlayer persistence  |
| FT-035 | ⚪ BACKLOG | P0       | Test TrackedPlayer API          |
| FT-036 | ⚪ BACKLOG | P1       | Test validation/error scenarios |

### Phase 1 acceptance criteria

The backend must expose enough functionality for the frontend to perform the complete MVP user flow without direct database access.

---

# Phase 2 — Frontend MVP

## Application foundation

| ID     | Status    | Priority | Task                            |
| ------ | --------- | -------- | ------------------------------- |
| FT-040 | ⚪ BACKLOG | P0       | Create Angular application      |
| FT-041 | ⚪ BACKLOG | P0       | Configure TypeScript            |
| FT-042 | ⚪ BACKLOG | P0       | Configure Angular routing       |
| FT-043 | ⚪ BACKLOG | P0       | Configure PWA                   |
| FT-044 | ⚪ BACKLOG | P1       | Define responsive/mobile layout |
| FT-045 | ⚪ BACKLOG | P1       | Define common UI components     |

## Player experience

| ID     | Status    | Priority | Task                         |
| ------ | --------- | -------- | ---------------------------- |
| FT-050 | ⚪ BACKLOG | P0       | Create player API service    |
| FT-051 | ⚪ BACKLOG | P0       | Create player list           |
| FT-052 | ⚪ BACKLOG | P0       | Create player detail         |
| FT-053 | ⚪ BACKLOG | P0       | Display current price        |
| FT-054 | ⚪ BACKLOG | P0       | Display current trend        |
| FT-055 | ⚪ BACKLOG | P1       | Display price history        |
| FT-056 | ⚪ BACKLOG | P1       | Display player team/position |

## Tracking experience

| ID     | Status    | Priority | Task                        |
| ------ | --------- | -------- | --------------------------- |
| FT-060 | ⚪ BACKLOG | P0       | Create tracking API service |
| FT-061 | ⚪ BACKLOG | P0       | Display tracking status     |
| FT-062 | ⚪ BACKLOG | P0       | Update tracking status      |
| FT-063 | ⚪ BACKLOG | P0       | Store release clause        |
| FT-064 | ⚪ BACKLOG | P1       | Store clause release date   |
| FT-065 | ⚪ BACKLOG | P1       | Store player notes          |
| FT-066 | ⚪ BACKLOG | P1       | Create tracked-player view  |

### Phase 2 acceptance criteria

A complete player can be viewed and managed from a mobile browser, including market information and personal tracking information.

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

1. **FT-017 — Validate GitHub Actions CI**
2. **FT-018 — Add backend automated tests**
3. **FT-023 — Complete Player REST API**
4. **FT-024 — Create PlayerPrice REST API**
5. **FT-025 — Create TrackedPlayer REST API**
6. **FT-026 — Add DTOs**
7. **FT-027 — Add validation**
8. **FT-028 — Add global error handling**
9. **FT-031/033/035 — Complete API test coverage**
10. **FT-040 — Create Angular application**

The project should remain focused on the Backend MVP until the API is sufficiently stable to support the frontend.

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
