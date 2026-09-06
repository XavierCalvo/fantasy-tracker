# Fantasy Tracker — Development Guide

## 1. Purpose

This guide explains how to work with the Fantasy Tracker repository during development.

It is intended to be a practical reference for:

* Starting the local environment.
* Understanding the repository.
* Implementing changes.
* Running tests.
* Managing database changes.
* Working with Git branches.

Some sections are marked as `TBD` because the corresponding part of the application has not yet been implemented.

---

# 2. Repository Structure

```text
fantasy-tracker/
├── frontend/
├── backend/
├── docs/
├── .github/
│   └── workflows/
├── docker-compose.yml
├── README.md
└── .gitignore
```

### Backend

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── Dockerfile
└── pom.xml
```

### Database migrations

```text
backend/src/main/resources/db/migration/
└── V1__init.sql
```

---

# 3. Prerequisites

Expected local tools:

* Git.
* Docker.
* Docker Compose.
* Java 21.
* Maven, if building outside the container.
* Node.js and npm, once frontend development starts.

Exact Node.js/Angular versions should be documented here when the frontend is created.

---

# 4. Start the Local Backend Environment

From the repository root:

```bash
docker compose up --build
```

This starts:

```text
PostgreSQL
    ↓
Spring Boot backend
```

The backend is expected to be available at:

```text
http://localhost:8080
```

The PostgreSQL database is exposed locally on:

```text
localhost:5432
```

---

# 5. Stop the Environment

```bash
docker compose down
```

To also remove the local PostgreSQL volume:

```bash
docker compose down -v
```

This deletes the local database data and should therefore only be used intentionally.

---

# 6. Backend Development

The backend uses:

* Java 21.
* Spring Boot.
* Spring Data JPA.
* PostgreSQL.
* Flyway.

From the `backend` directory:

```bash
mvn test
```

Build:

```bash
mvn clean package
```

The exact development commands may evolve as the CI pipeline is completed.

---

# 7. Database Development

Database schema changes are managed through Flyway.

Current migration:

```text
V1__init.sql
```

While the initial schema has not been released to a persistent environment, the V1 migration can still be adjusted directly.

Once V1 is released, **do not modify V1**.

Create a new migration instead:

```text
V2__description.sql
V3__description.sql
...
```

Migration names should clearly describe their purpose.

---

# 8. Adding a New Entity

When introducing a new persistent domain object:

1. Define the domain requirements.
2. Update the data model documentation.
3. Add/update the Java entity.
4. Add the repository.
5. Add the database migration.
6. Add validation.
7. Add tests.
8. Add API endpoints if required.
9. Update the backlog.
10. Update architecture/decision documentation if the change is architecturally significant.

Do not introduce database changes without corresponding migration coverage.

---

# 9. API Development

The backend API should follow a resource-oriented REST design.

Initial resource groups are expected to be:

```text
/api/players
/api/player-prices
/api/tracked-players
```

Before implementing a new endpoint:

1. Define its purpose.
2. Define request/response models.
3. Define validation rules.
4. Define error behaviour.
5. Add automated tests.
6. Implement the endpoint.

The final API contract should eventually be documented separately if it becomes large enough to justify it.

---

# 10. Frontend Development

The Angular frontend has not yet been implemented.

The expected structure will separate:

* Pages/routes.
* Reusable components.
* API services.
* Models/interfaces.
* State where necessary.
* Shared UI functionality.

The frontend must communicate with the backend through the REST API.

It must not connect directly to PostgreSQL.

---

# 11. Testing Workflow

Before considering a feature complete:

```text
Implement
   ↓
Unit tests
   ↓
Integration/API tests
   ↓
Build
   ↓
CI
   ↓
Review
   ↓
Merge
```

E2E tests should be added for important user journeys rather than every small implementation change.

---

# 12. Git Branching

The project uses feature branches for development.

Example:

```text
main
  │
  └── feature/startup
          │
          ├── feature/backend-api
          ├── feature/frontend-mvp
          └── feature/data-acquisition
```

The exact branching strategy may evolve, but production-ready code should ultimately be integrated into `main`.

---

# 13. Commit Guidelines

Commits should describe the change clearly.

Examples:

```text
feat: add player price API
feat: add player tracking
test: add player repository tests
fix: validate negative player prices
docs: update architecture
refactor: extract player service
```

Avoid commits such as:

```text
changes
fix stuff
update
test
```

---

# 14. Definition of Done

A development task should normally be considered complete when:

* Implementation is complete.
* Relevant tests exist.
* Tests pass locally.
* CI passes.
* Documentation is updated when necessary.
* No unnecessary technical debt has been introduced.
* The backlog status is updated.

For API or database changes, the corresponding domain documentation should also be reviewed.

---

# 15. Working With the Backlog

The detailed backlog is maintained in `docs/03-roadmap.md` for now.

Each task has an identifier:

```text
FT-XXX
```

When starting a task:

```text
⚪ BACKLOG → 🔵 READY → 🟡 IN PROGRESS
```

When completed:

```text
🟡 IN PROGRESS → 🟢 DONE
```

Blocked work should use:

```text
🔴 BLOCKED
```

Completed tasks should remain in the backlog for historical traceability.

---

# 16. Documentation Rules

The documentation directory should remain useful rather than becoming a duplicate of the source code.

Use the documents as follows:

| Document                  | Purpose                           |
| ------------------------- | --------------------------------- |
| `01-project-vision.md`    | What we are building              |
| `02-architecture.md`      | How it is structured              |
| `03-roadmap.md`           | Where we are going                |
| `04-backend.md`           | Backend implementation details    |
| `05-frontend.md`          | Frontend implementation details   |
| `06-data-model.md`        | How data is modelled              |
| `07-testing-strategy.md`  | How quality is verified           |
| `08-deployment.md`        | How the application runs/deploys   |
| `09-decisions.md`         | Why important decisions were made |
| `10-development-guide.md` | How to work on the project        |

The README should remain the short entry point and should link to these documents rather than reproducing them.

---

# 17. Current Development Priority

The immediate development sequence is:

```text
1. Finish project foundation
       ↓
2. Stabilise CI
       ↓
3. Add backend tests
       ↓
4. Complete backend MVP
       ↓
5. Build Angular frontend
       ↓
6. Complete end-to-end MVP
       ↓
7. Implement data acquisition
       ↓
8. Add analytics
       ↓
9. Deploy to AWS
```

Avoid starting advanced analytics or AWS infrastructure before the core MVP provides a stable foundation.

---

# 18. Open Areas

The following areas are intentionally not fully defined yet:

* Final Angular project structure.
* API contract.
* External data acquisition mechanism.
* Automated scraping.
* Test database approach.
* AWS backend execution model.
* Production monitoring.
* Authentication.

These should be resolved when the corresponding development phase is reached rather than prematurely over-engineering the project.
