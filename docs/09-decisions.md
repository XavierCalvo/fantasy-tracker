# Fantasy Tracker — Decisions

## 1. Purpose

This document records important architectural and product decisions.

The objective is to preserve the reasoning behind significant choices so that future changes can be evaluated against the original context.

Decisions should not be silently rewritten.

If a decision is replaced, the original decision remains in the history and a new decision records the change.

---

# 2. Decision Status

| Status     | Meaning                      |
| ---------- | ---------------------------- |
| Proposed   | Being evaluated              |
| Accepted   | Current decision             |
| Superseded | Replaced by a later decision |
| Rejected   | Explicitly not selected      |

---

# ADR-001 — Angular PWA

**Status:** Accepted

### Context

The application is primarily a personal mobile tool and does not initially require native Android or iOS functionality.

### Decision

Use Angular + TypeScript and deliver the application as a Progressive Web App.

### Consequences

**Positive**

* One application for desktop and mobile.
* Installable on supported mobile devices.
* No native application maintenance.
* Good fit for the planned frontend architecture.

**Negative**

* Some native capabilities may not be available.
* Browser/PWA behaviour must be considered.

---

# ADR-002 — Java 21 + Spring Boot

**Status:** Accepted

### Context

The backend needs a stable REST framework with good PostgreSQL and testing support.

### Decision

Use Java 21 with Spring Boot.

### Consequences

* Modern LTS Java baseline.
* Mature REST ecosystem.
* Strong integration with JPA and PostgreSQL.
* Good test tooling.

---

# ADR-003 — PostgreSQL

**Status:** Accepted

### Context

The application needs relational storage with historical data and relationships.

### Decision

Use PostgreSQL.

### Consequences

* Strong relational model.
* Good support for historical datasets.
* Suitable for local Docker development.
* Direct path to Amazon RDS PostgreSQL.

---

# ADR-004 — Flyway for Database Migrations

**Status:** Accepted

### Context

Database changes need to be reproducible and traceable.

### Decision

Use Flyway for schema migrations.

### Consequences

* Schema evolution is version-controlled.
* Environments can be recreated consistently.
* Production changes are traceable.

---

# ADR-005 — Separate Player and PlayerPrice

**Status:** Accepted

### Context

Player identity and market price are fundamentally different concepts.

### Decision

Store player master data in `PLAYER` and market observations in `PLAYER_PRICE`.

### Consequences

* Historical prices can grow without duplicating player data.
* Player identity remains stable.
* Analytics can operate on historical observations.

---

# ADR-006 — Separate Player and TrackedPlayer

**Status:** Accepted

### Context

A player's market information is different from the user's personal decisions.

### Decision

Store personal tracking information in `TRACKED_PLAYER`.

### Consequences

* Player data is not polluted with user-specific state.
* Tracking can evolve independently.
* Multi-user support remains conceptually possible in the future.

---

# ADR-007 — Structured Price Trend Enum

**Status:** Accepted

### Context

FutbolFantasy exposes qualitative trend categories such as acceleration and deceleration.

Storing translated display text would make future analytics and localisation unnecessarily difficult.

### Decision

Store a structured enum representing the trend.

### Consequences

* Trend data can be analysed programmatically.
* UI language can change independently.
* Trend descriptions remain presentation data.

---

# ADR-008 — Store Historical Price Observations

**Status:** Accepted

### Context

Future analytics require historical price evolution.

### Decision

Store price observations instead of keeping only the current price.

### Consequences

The application can later calculate:

* 24h variation.
* 3-day variation.
* 7-day variation.
* Velocity.
* Acceleration.
* Trend evolution.

Storage requirements will increase over time, but the historical data is fundamental to the product vision.

---

# ADR-009 — Authentication Deferred

**Status:** Accepted

### Context

The initial application is intended as a personal MVP.

### Decision

Authentication and user accounts are not part of the initial implementation.

### Consequences

* Faster MVP development.
* Simpler backend.
* No account management initially.

If multi-user support is introduced later, the data model and API will need to be adapted.

---

# ADR-010 — Scraping Deferred

**Status:** Accepted

### Context

Automated acquisition of FutbolFantasy data is important, but implementing scraping before the core application is stable would increase complexity prematurely.

### Decision

Build and stabilise the core application before implementing scraping/data acquisition.

### Consequences

* Initial development can use manually created/imported data.
* The acquisition layer can be designed independently.
* The external source does not become a dependency for the MVP.

---

# ADR-011 — Docker Compose for Local Development

**Status:** Accepted

### Context

Developers need a reproducible local PostgreSQL environment.

### Decision

Use Docker Compose for local infrastructure.

### Consequences

* PostgreSQL setup is reproducible.
* New environments can be created quickly.
* Local infrastructure resembles the future container/cloud architecture.

---

# ADR-012 — Integer Price Representation

**Status:** Accepted

### Context

FutbolFantasy prices are represented as euro amounts without requiring decimal monetary precision for the current domain.

### Decision

Represent player prices as integer values in the Java domain model (`Long`) and integer-valued numeric storage in PostgreSQL.

### Consequences

* No unnecessary floating-point calculations.
* Simple comparison and arithmetic.
* Sufficient precision for the current domain.

Release clauses may use `BigDecimal` because they represent monetary values independently of the market-price storage decision.

---

# ADR-013 — No Derived Metrics in Initial Schema

**Status:** Accepted

### Context

Metrics such as 24h change, velocity and acceleration require historical observations and may evolve as the product is developed.

### Decision

Do not store derived analytical metrics in V1.

Calculate them later from historical data unless performance requirements justify persistence.

### Consequences

* Initial schema remains simple.
* Analytics can evolve without repeated schema migrations.
* Historical observations remain the primary source of truth.

---

# ADR-014 — Uniform REST API Conventions

**Status:** Accepted

### Context

The backend needed a consistent, predictable contract across `Player`, `PlayerPrice` and `TrackedPlayer` endpoints before the frontend starts consuming the API.

### Decision

* Use dedicated request/response DTOs for every endpoint; never serialize JPA entities directly.
* Apply bean validation (`jakarta.validation`) on request DTOs.
* Centralise error translation in a single `@RestControllerAdvice` (`GlobalExceptionHandler`) returning a uniform `ApiError` body (`timestamp`, `status`, `error`, `message`, `path`, `details`).
* Use standard HTTP status codes consistently: `200`/`201`/`204` for success, `400` for validation/malformed input, `404` for missing resources, `500` for unexpected failures.

### Consequences

* Frontend can rely on a single, predictable error shape across all endpoints.
* Entity/schema changes no longer leak directly into the API contract.
* New endpoints only need to follow the existing convention rather than invent their own.

---

# 3. Future Decisions

Important future decisions that still require explicit evaluation include:

* Exact external data acquisition mechanism.
* Scraping technology.
* API endpoint design.
* DTO strategy.
* Frontend component architecture.
* Test database strategy.
* AWS Lambda vs containerised backend.
* Authentication model if multi-user support is introduced.
* Data retention strategy for large price histories.

New significant decisions should be added to this document rather than being left only in commit messages or chat discussions.
