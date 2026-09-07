# Fantasy Tracker — Frontend

## 1. Purpose

The Fantasy Tracker frontend is a mobile-first web application built with Angular and TypeScript.

Its purpose is to provide a simple and fast interface for:

* Browsing players.
* Reviewing market information.
* Analysing price history.
* Managing tracked players.
* Reviewing release clauses.
* Adding personal notes.
* Accessing future analytical features.

The frontend is a presentation and interaction layer. It must not contain persistence logic or access the database directly.

---

# 2. Technology

The planned frontend stack is:

* **Angular**
* **TypeScript**
* **Angular Router**
* **Angular PWA**
* Responsive CSS/layout
* REST API communication with the Spring Boot backend

The exact Angular version and UI component strategy will be defined when the frontend project is created.

---

# 3. Design Principles

The frontend should follow these principles:

### Mobile first

The application is primarily intended to be used from a mobile device.

Desktop support is desirable, but mobile usability has priority.

### Fast access to relevant information

The user should be able to reach important market information with as few interactions as possible.

### Simple interface

The application should avoid unnecessary screens, configuration and visual complexity.

### API-driven

The frontend communicates with the backend through REST APIs.

No direct database access is allowed.

### Reusable components

Common UI elements should be implemented as reusable Angular components rather than duplicated across pages.

### Progressive enhancement

The initial interface should remain useful without advanced analytics. Additional analytical capabilities can be introduced progressively.

---

# 4. Application Structure

The expected high-level structure is:

```text id="q1g6up"
frontend/
├── src/
│   ├── app/
│   │   ├── core/
│   │   ├── shared/
│   │   ├── players/
│   │   ├── tracking/
│   │   ├── analysis/
│   │   └── app.routes.ts
│   ├── assets/
│   └── styles/
├── public/
├── package.json
└── ...
```

This is an initial target structure rather than a fixed implementation.

The structure should evolve if the actual application demonstrates a better organisation.

---

# 5. Functional Areas

The frontend is expected to contain the following functional areas.

## 5.1 Watchlist (Tracked Players)

The watchlist is the **main entry point** of the application (default route `/tracked`). A bare player catalog is not actionable on its own — what the user actually needs day to day is the list of players they are already tracking.

It should allow the user to:

* List every tracked player, enriched with the underlying player's name/team/position.
* Filter by tracking status (`WATCHING`, `OWNED`, `DISCARDED`, or all).
* Sort by clause release date, player name or status, in either direction.
* Open a tracked player's detail (which reuses the Player Detail screen).

## 5.2 Players

The player catalog is a secondary area, reachable from the top navigation. It backs the watchlist (a player must exist before it can be tracked) and lets the user manage the player database directly.

It should allow the user to:

* Browse players.
* Search players.
* Filter players.
* Open player details.
* Review current market information.
* Create a new player.
* Edit an existing player (e.g. team/position changes after a transfer, correcting the Futbolfantasy external id, or fixing a mistake made at creation time).

---

## 5.3 Player Detail

The player detail screen should provide a consolidated view of the player.

Expected information:

```text id="aqly2u"
Player
├── Name
├── Team
├── Position
├── Current price
├── Current trend
├── Price movement
├── Price history
└── Personal tracking
      ├── Status
      ├── Clause
      ├── Clause release date
      └── Notes
```

The screen also links to the player edit form (`/players/:id/edit`).

The exact visual layout is TBD.

---

## 5.4 Player Creation & Editing

A dedicated form (shared between creation and editing) lets the user register new players and correct existing ones.

Fields:

* Name (required).
* Team.
* Position.
* Futbolfantasy external id.

Routes:

* `/players/new` — create mode, empty form.
* `/players/:id/edit` — edit mode, form pre-filled from the existing player.

On save, the user is redirected to the corresponding Player Detail screen.

---

## 5.5 Price History

The player detail should eventually display historical price evolution.

The initial implementation may use a simple list or basic visualisation.

Later versions can introduce charts.

Potential information:

* Historical price.
* Capture date/time.
* Trend.
* Trend amount.

Derived metrics such as 24h, 3-day and 7-day changes will be added in a later phase.

---

## 5.6 Tracking

The user should be able to manage the personal state of a player.

Available states:

```text id="uwy4ql"
WATCHING
OWNED
DISCARDED
```

The interface should allow:

* Changing tracking status.
* Adding/updating a release clause.
* Adding/updating clause release date.
* Adding/updating notes.

---

# 6. Navigation

The implemented navigation tree is:

```text id="7g2a5q"
                    ┌───────────────────────┐
                    │  Tracked Players (/)   │  ← default route, main entry point
                    │  filter by status,     │
                    │  sort by clause date    │
                    └───────────┬────────────┘
                                │ open a row
                                ▼
                    ┌───────────────────────┐
              ┌────►│    Player Detail       │◄────┐
              │     │ price · trend · history│     │
              │     │ · tracking form         │     │
              │     └───────────┬────────────┘     │
              │                 │ "Editar jugador"  │
              │                 ▼                   │
              │     ┌───────────────────────┐       │
              │     │  Player Form (edit)    │       │
              │     │  /players/:id/edit     │       │
              │     └───────────────────────┘       │
              │                                      │
    ┌─────────┴─────────┐                            │
    │   Players (list)   │  reachable from top nav    │
    │   search/filter    │────────────────────────────┘
    └─────────┬──────────┘
              │ "Nuevo jugador"
              ▼
    ┌───────────────────────┐
    │  Player Form (create)  │
    │  /players/new           │
    └───────────────────────┘
```

The top toolbar provides persistent links to **Seguimiento** (`/tracked`) and **Jugadores** (`/players`).

A mobile navigation bar may eventually provide direct access to:

* Watchlist.
* Players.
* Opportunities (future analysis phase).

---

# 7. API Communication

The frontend will communicate with the Spring Boot backend through HTTP.

Expected services include:

```text id="9r4e8f"
PlayerService
PlayerPriceService
TrackedPlayerService
```

The services should encapsulate HTTP communication and expose application-friendly methods to components.

Components should not contain raw HTTP calls.

---

# 8. Frontend Models

The frontend should define TypeScript interfaces/models representing API contracts.

Examples:

```text id="2k0qhh"
Player
PlayerPrice
PlayerPriceTrend
TrackedPlayer
TrackedPlayerStatus
```

These models should represent the API contract rather than duplicating backend implementation classes.

---

# 9. State Management

The initial application does not require a complex global state-management framework.

Local component/service state should be preferred until the application demonstrates a real need for more sophisticated state management.

Potential future state:

* Current player list.
* Selected player.
* Tracking state.
* Filters.
* Analytics data.

A dedicated state-management library should only be introduced if application complexity justifies it.

---

# 10. Responsive Design

The application should support at least:

* Mobile portrait.
* Mobile landscape.
* Tablet.
* Desktop.

Priority:

```text id="2h5lpa"
Mobile
  ↓
Tablet
  ↓
Desktop
```

Important mobile considerations include:

* Touch-friendly controls.
* Readable typography.
* Minimal horizontal scrolling.
* Compact player cards.
* Clear visual hierarchy.
* Fast navigation.

---

# 11. PWA

The application is intended to be installable as a Progressive Web App.

Expected capabilities:

* Installable application.
* Application icon.
* Responsive layout.
* Service worker.
* Appropriate caching strategy.

Offline functionality is **not initially required**.

The PWA should not cache market data in a way that could cause the user to make decisions using stale information without clear indication.

---

# 12. Error Handling

The frontend should provide clear feedback for:

* Backend unavailable.
* Network errors.
* Invalid requests.
* Player not found.
* Save/update failures.
* Loading failures.

Errors should be understandable to the user without exposing backend implementation details.

---

# 13. Loading States

Long-running operations should provide visible feedback.

Examples:

```text id="3j2v9v"
Loading players...
Loading player...
Saving...
Updating tracking...
```

The application should avoid unnecessary loading indicators for operations that complete immediately.

---

# 14. Future Analytics UI

Later phases will introduce analytical views.

Potential screens/components include:

### Market

* Biggest risers.
* Biggest fallers.
* Accelerating players.
* Decelerating players.

### Opportunities

* Best value opportunities.
* Attractive release clauses.
* Players likely to increase in value.

### Squad

* Current squad.
* Positions requiring reinforcement.
* Players to sell.
* Potential replacements.

These features should be implemented only after sufficient historical data is available.

---

# 15. Testing

The frontend will use several testing levels.

### Unit/component tests

For:

* Components.
* Services.
* Formatting.
* Validation.
* User interactions.

### Integration/API tests

For:

* API service behaviour.
* Data mapping.
* Error handling.

### E2E

For critical user journeys such as:

```text id="2e6z4w"
Open application
    ↓
Browse players
    ↓
Open player
    ↓
Review price
    ↓
Update tracking
    ↓
Save
```

The testing strategy is defined in:

```text id="v4yy0e"
docs/07-testing-strategy.md
```

---

# 16. Accessibility

The frontend should follow basic accessibility principles from the beginning.

At minimum:

* Semantic HTML.
* Keyboard accessibility.
* Sufficient text contrast.
* Labels for interactive controls.
* Accessible form validation.
* Avoid relying exclusively on colour to communicate information.

Accessibility should be considered during component development rather than treated as a final phase.

---

# 17. Performance

The initial application should remain lightweight.

Important considerations:

* Avoid unnecessary API requests.
* Lazy-load larger application areas where appropriate.
* Avoid unnecessary dependencies.
* Optimise images/assets.
* Avoid excessive client-side processing.
* Keep the initial application bundle reasonable.

Performance optimisation should be driven by actual measurements rather than premature optimisation.

---

# 18. Security

The frontend must never contain:

* Database credentials.
* Production secrets.
* Private API credentials.
* Sensitive backend configuration.

The frontend should assume that anything shipped to the browser is publicly inspectable.

Authentication is not part of the initial MVP, but this must be reconsidered before exposing a multi-user production application.

---

# 19. Current Status

| Area                     | Status    |
| ------------------------ | --------- |
| Angular application      | 🟢 DONE   |
| TypeScript configuration | 🟢 DONE   |
| Routing                  | 🟢 DONE   |
| PWA                      | 🟢 DONE   |
| Responsive layout        | 🟡 IN PROGRESS |
| Player list              | 🟢 DONE   |
| Player detail            | 🟢 DONE   |
| Player create/edit form  | 🟢 DONE   |
| Price history            | 🟢 DONE   |
| Tracking UI              | 🟢 DONE   |
| Tracked-players watchlist | 🟢 DONE  |
| API services             | 🟢 DONE   |
| Frontend tests           | 🟢 DONE   |
| E2E tests                | ⚪ BACKLOG |
| Accessibility            | ⚪ BACKLOG |
| Analytics UI             | ⚪ BACKLOG |
| Create player + tracking in one step | ⚪ BACKLOG |
| Position as fixed dropdown (Portero/Defensa/Medio/Delantero/Entrenador) | ⚪ BACKLOG |

Phase 2 core functionality is implemented: the Angular workspace (standalone components, Vitest, Angular Material, PWA/service worker, dev-server proxy, Docker/nginx deployment), the `PlayerApi`/`PlayerPriceApi`/`TrackedPlayerApi` services, the player list/detail screens, a shared `PlayerForm` for creating and editing players (`/players/new`, `/players/:id/edit`), price history and the tracking form (status, clause, clause release date, notes) all exist and are covered by unit tests. A dedicated **watchlist screen** (`TrackedPlayers`, route `/tracked`) lists every tracked player enriched with player name/team/position, filterable by status and sortable by clause release date, name or status; it is the app's default landing route. Remaining work: finish responsive/mobile layout polish, add E2E tests and accessibility review.

**Pending task — Create player + tracking in one step:** Currently creating a new player and adding it to tracking are two separate steps (create via `PlayerForm`, then edit to add tracking). Add an optional "Add to tracking" checkbox to `PlayerForm` when creating a new player; if checked, show the tracking fields (status, clause, clause release date, notes) inline and, on submit, create the `Player` followed by the `TrackedPlayer` (two API calls from the frontend, transparent to the user). Keep the checkbox unchecked by default so a plain player-only creation remains a single click.

---

# 20. First Frontend Implementation

When Phase 2 starts, the recommended implementation order is:

```text id="j1p0ut"
1. Create Angular application
        ↓
2. Configure routing
        ↓
3. Configure PWA
        ↓
4. Create base responsive layout
        ↓
5. Create Player model/API service
        ↓
6. Create Player list
        ↓
7. Create Player detail
        ↓
8. Add price information
        ↓
9. Add tracking
        ↓
10. Add tests
        ↓
11. Polish mobile UX
```

The frontend should initially focus on completing the MVP user journey rather than building advanced analytics.
