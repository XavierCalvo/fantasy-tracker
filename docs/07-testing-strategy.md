# Fantasy Tracker — Testing Strategy

## 1. Purpose

The testing strategy aims to provide confidence in the application while keeping feedback fast enough for normal development.

The project will use several levels of testing rather than relying exclusively on end-to-end tests.

The target strategy is:

```text
                  ┌─────────────────┐
                  │   E2E Tests     │
                  │   Few / critical│
                  └────────┬────────┘
                           │
                  ┌────────▼────────┐
                  │ Integration/API │
                  │     Tests       │
                  └────────┬────────┘
                           │
                ┌──────────▼──────────┐
                │     Unit Tests       │
                │     Many / Fast      │
                └─────────────────────┘
```

---

# 2. Testing Principles

1. **Fast feedback first.**
2. **Test business behaviour, not implementation details.**
3. **Keep E2E tests limited to important user journeys.**
4. **Use integration tests where persistence or framework behaviour matters.**
5. **Do not duplicate the same coverage at every level without a reason.**
6. **Tests must be deterministic.**
7. **External systems should be isolated from normal unit tests.**
8. **CI must run the relevant automated test suite automatically.**

---

# 3. Backend Testing

## 3.1 Unit Tests

Unit tests will validate isolated business behaviour.

Potential targets:

* Validation rules.
* Trend-related logic.
* Analytical calculations.
* Services.
* Utility classes.

Unit tests should normally:

* Run without PostgreSQL.
* Run quickly.
* Avoid external network dependencies.

---

## 3.2 Repository / Integration Tests

Integration tests will validate persistence behaviour.

Examples:

* Player persistence.
* Unique external ID.
* Player-price relationships.
* Tracking persistence.
* Enum persistence.
* Database constraints.

These tests should use a real PostgreSQL-compatible environment when database-specific behaviour is important.

The exact test infrastructure will be decided when the first test suite is implemented.

---

## 3.3 REST API Tests

API tests will validate the public backend contract.

For each resource, tests should cover:

* Successful requests.
* Invalid requests.
* Missing resources.
* Validation errors.
* Persistence effects.
* Response structure.

Initial resources:

```text
/api/players
/api/player-prices
/api/tracked-players
```

The exact endpoint design will be documented when the API is finalised.

---

# 4. Frontend Testing

## 4.1 Unit / Component Tests

Frontend unit tests will validate:

* Components.
* Services.
* Formatting.
* User interactions.
* State transformations.
* Validation.

The tests should avoid coupling to browser implementation details wherever possible.

---

## 4.2 API Service Tests

Frontend API services should verify:

* Correct HTTP requests.
* Correct parameter handling.
* Response mapping.
* Error handling.

The backend should not need to be running for normal service unit tests.

---

## 4.3 End-to-End Tests

E2E tests should represent important real user journeys.

Initial candidates:

### Player browsing

```text
Open application
    ↓
Open player list
    ↓
Select player
    ↓
View player information
```

### Tracking

```text
Open player
    ↓
Set WATCHING
    ↓
Add clause
    ↓
Add notes
    ↓
Save
    ↓
Reload
    ↓
Verify state
```

### Price history

```text
Open player
    ↓
Open price history
    ↓
Verify historical observations
```

E2E should not attempt to cover every validation rule already tested at lower levels.

---

# 5. Test Data

Test data must be explicit and reproducible.

The project should avoid relying on uncontrolled shared databases for automated tests.

As the application grows, test data should move towards:

* Programmatic creation.
* Controlled fixtures.
* Dedicated test databases.
* Repeatable setup/cleanup.

The production/golden database must never be required for normal automated test execution.

---

# 6. External Data Testing

When automated FutbolFantasy data acquisition is introduced, the external source must be isolated from the core test suite.

Normal tests should not depend on:

* External network availability.
* External website response times.
* Current market data.
* Changes in the external site's implementation.

The acquisition layer should therefore be tested using controlled responses/mocks in most automated tests.

A smaller number of dedicated integration checks may validate the real external source when appropriate.

---

# 7. CI Testing

GitHub Actions should progressively execute:

```text
Backend
  ├── Compile
  ├── Unit tests
  └── Integration/API tests

Frontend
  ├── Install
  ├── Build
  └── Unit tests

Optional
  └── E2E
```

The exact pipeline will evolve as the frontend and test suites are implemented.

---

# 8. Quality Gates

The project should eventually prevent merging code when:

* Compilation fails.
* Required tests fail.
* Build fails.
* Critical quality checks fail.

Additional quality gates may be introduced later.

---

# 9. Current Status

| Area                          | Status         |
| ----------------------------- | -------------- |
| Backend test framework        | 🟢 DONE        |
| Backend unit tests            | 🟢 DONE        |
| Repository/integration tests  | 🟢 DONE        |
| REST API tests                | 🟢 DONE        |
| Frontend tests                | ⚪ BACKLOG      |
| E2E tests                     | ⚪ BACKLOG      |
| CI test execution (backend)   | 🟢 DONE        |
| CI test execution (frontend)  | ⚪ BACKLOG      |
| External-source test strategy | ⚪ BACKLOG      |

This document defines the intended strategy. Specific tools and implementation details may evolve as the project develops.
