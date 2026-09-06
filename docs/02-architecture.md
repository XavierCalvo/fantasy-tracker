# Fantasy Tracker — Architecture

## 1. Overview

Fantasy Tracker follows a layered architecture with a clear separation between:

* Frontend presentation.
* Backend REST API and business logic.
* Persistent data.
* External data acquisition.
* Future cloud infrastructure.

The initial implementation is intentionally simple. The architecture should allow the application to evolve from local development to an AWS-hosted solution without requiring a major redesign.

---

## 2. High-Level Architecture

```text
                         ┌─────────────────────┐
                         │     FutbolFantasy   │
                         │   External Source   │
                         └──────────┬──────────┘
                                    │
                          Future data acquisition
                                    │
                                    ▼
┌───────────────────┐       ┌─────────────────────┐
│                   │       │                     │
│  Angular PWA      │◄─────►│  Spring Boot API    │
│  TypeScript       │ HTTP  │  Java 21            │
│                   │ REST  │                     │
└───────────────────┘       └──────────┬──────────┘
                                        │
                                        │ JPA
                                        ▼
                              ┌───────────────────┐
                              │    PostgreSQL     │
                              │                   │
                              │  Player           │
                              │  Player Price     │
                              │  Tracked Player   │
                              └───────────────────┘
```

The external FutbolFantasy source is deliberately separated from the application API. This allows the data acquisition mechanism to change without coupling the frontend to the external source.

---

## 3. Repository Structure

The project is maintained as a monorepo:

```text
fantasy-tracker/
├── frontend/
├── backend/
├── docs/
├── infrastructure/
├── .github/
│   └── workflows/
├── docker-compose.yml
├── README.md
└── .gitignore
```

Not every directory needs to contain production code from the beginning. The structure represents the intended evolution of the project.

---

## 4. Frontend

### Technology

* Angular
* TypeScript
* Angular Router
* PWA capabilities
* Responsive/mobile-first UI

### Responsibilities

The frontend is responsible for:

* Displaying players.
* Displaying price information.
* Displaying historical evolution.
* Managing the user's tracked players.
* Managing clauses and notes.
* Presenting future analytics.
* Calling the backend REST API.

The frontend should **not access PostgreSQL directly**.

---

## 5. Backend

### Technology

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Flyway
* PostgreSQL

### Responsibilities

The backend provides:

* REST endpoints.
* Domain validation.
* Persistence.
* Player management.
* Price history management.
* Tracking management.
* Future analytical calculations.
* Future integration with external data sources.

The backend is the main boundary between the frontend and persistent/external data.

### API Conventions

* Base path: all endpoints are under `/api`.
* Resource-oriented URLs, e.g. `/api/players`, `/api/players/{id}`, `/api/players/{id}/prices`, `/api/players/{id}/tracking`, `/api/tracking/{id}`.
* Controllers never expose JPA entities directly; every request/response uses a dedicated DTO (`com.fantasytracker.dto`).
* Standard HTTP status codes:
  * `200 OK` — successful `GET`/`PUT`.
  * `201 Created` — successful `POST`.
  * `204 No Content` — successful `DELETE`.
  * `400 Bad Request` — validation failure or malformed JSON.
  * `404 Not Found` — referenced resource does not exist.
  * `500 Internal Server Error` — unexpected failure.
* Errors are returned as a uniform `ApiError` JSON body (`com.fantasytracker.web.ApiError`):

  ```json
  {
    "timestamp": "2026-01-01T12:00:00+01:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "path": "/api/players",
    "details": ["name: Player name is required"]
  }
  ```

* Bean validation (`jakarta.validation`) is applied on request DTOs; validation failures populate `details` with one `field: message` entry per violation.
* A global `@RestControllerAdvice` (`GlobalExceptionHandler`) is the single place responsible for translating exceptions into `ApiError` responses, keeping controllers free of manual error handling.

---

## 6. Database

PostgreSQL is the persistence layer.

The initial domain is deliberately split into three main concepts.

### PLAYER

Represents the identity and relatively stable information of a football player.

```text
PLAYER
-----
id
name
team
position
external_id
created_at
```

`external_id` represents the identifier used by an external data source when available.

---

### PLAYER_PRICE

Represents a historical market observation.

```text
PLAYER_PRICE
------------
id
player_id
price
trend_amount
trend_type
captured_at
```

A player can have many price observations.

The price is stored as an integer-valued amount because FutbolFantasy prices are represented in euros without requiring decimal precision.

---

### TRACKED_PLAYER

Represents the user's relationship with a player.

```text
TRACKED_PLAYER
--------------
id
player_id
status
clause
clause_release_date
notes
created_at
updated_at
```

Current tracking statuses are:

* `WATCHING`
* `OWNED`
* `DISCARDED`

This entity is intentionally separated from `PLAYER` because tracking information is personal application state rather than player master data.

---

## 7. Price Trend Model

FutbolFantasy provides a qualitative description of the direction and acceleration of price movement.

The application stores this as a structured enum rather than as display text.

Current values:

```text
INFLECTION_POSITIVE
ACCELERATING_STRONGLY_UP
ACCELERATING_UP
STABLE_UP
DECELERATING_UP
DECELERATING_STRONGLY_UP

INFLECTION_NEGATIVE
DECELERATING_STRONGLY_DOWN
DECELERATING_DOWN
STABLE_DOWN
ACCELERATING_DOWN
ACCELERATING_STRONGLY_DOWN
```

`trend_amount` stores the associated numeric movement when available.

The Spanish display descriptions belong to the application presentation layer and should not be used as the database's domain identifier.

---

## 8. Database Migrations

Flyway is used to manage database schema evolution.

The current schema is defined in:

```text
backend/src/main/resources/db/migration/
└── V1__init.sql
```

There is intentionally no second migration yet.

The project is still in the initial schema phase, so changes to the initial model can be made directly to `V1__init.sql` until the first migration has been released/executed in a persistent environment.

Once V1 is considered released, subsequent schema changes must use new versioned migrations.

---

## 9. Local Development

Docker Compose provides the local PostgreSQL environment and the backend container.

Current services:

```text
db
└── PostgreSQL 15

backend
└── Spring Boot application
```

The backend connects to PostgreSQL through the Docker network.

The frontend will eventually run independently during development and communicate with the backend through HTTP.

---

## 10. Testing Architecture

Testing will evolve across several levels.

### Backend

* Unit tests.
* Repository/integration tests.
* REST API tests.

### Frontend

* Unit/component tests.
* Service/API tests.
* End-to-end tests where appropriate.

### Future system testing

Once data acquisition and analytics are implemented, integration and end-to-end flows will validate the complete application.

The objective is to keep the test pyramid balanced and avoid making the application dependent exclusively on slow end-to-end tests.

---

## 11. CI/CD

GitHub Actions is the CI platform.

The CI pipeline should progressively validate:

1. Backend compilation.
2. Backend tests.
3. Frontend installation/build.
4. Frontend tests.
5. Static/quality checks.
6. Container build where useful.

Deployment automation will be introduced later when the AWS infrastructure is defined.

---

## 12. Future AWS Architecture

The target cloud architecture is expected to use AWS managed services.

A possible target is:

```text
                    ┌────────────────────┐
                    │   Angular PWA      │
                    │  AWS Amplify       │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │    API Gateway     │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │      Lambda        │
                    │   Spring Boot API  │
                    └─────────┬──────────┘
                              │
                              ▼
                    ┌────────────────────┐
                    │    PostgreSQL      │
                    │       RDS          │
                    └────────────────────┘

          EventBridge
               │
               ▼
        Scheduled data
          acquisition
```

The exact AWS implementation is intentionally not fixed yet.

---

## 13. Architecture Decisions

Important decisions are recorded in the project documentation.

Current decisions include:

| Decision                          | Rationale                                                      |
| --------------------------------- | -------------------------------------------------------------- |
| Angular PWA                       | Mobile-first web application without maintaining native apps   |
| Java 21                           | Current LTS Java baseline and good fit for Spring Boot         |
| Spring Boot                       | Mature REST/API ecosystem                                      |
| PostgreSQL                        | Reliable relational database suitable for historical data      |
| Flyway                            | Explicit and traceable schema evolution                        |
| Player / PlayerPrice separation   | Player identity must not be mixed with historical observations |
| Player / TrackedPlayer separation | Personal tracking state is different from player master data   |
| Structured trend enum             | Allows future analytics without relying on display strings     |
| Docker Compose                    | Reproducible local development environment                     |
| Authentication deferred           | Not required for the initial personal MVP                      |

Further significant decisions should be added as the project evolves.
