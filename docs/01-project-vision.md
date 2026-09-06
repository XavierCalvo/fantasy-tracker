# Fantasy Tracker — Project Vision

## 1. Purpose

Fantasy Tracker is a mobile-first personal tool for managing and analysing a FutbolFantasy squad and the players worth monitoring in the market.

The application will combine:

* Player master data.
* Historical market prices and price trends.
* Personal tracking information such as status, clause and notes.
* Later, derived metrics to identify value and market opportunities.

The goal is **not to reproduce FutbolFantasy**, but to provide a focused decision-support tool for fantasy management.

---

## 2. Product Vision

The final product should make it easy to answer questions such as:

* Which players are worth watching?
* Which players are rising or falling in value?
* How has a player's price evolved over time?
* Which tracked players have an attractive release clause?
* Which opportunities deserve attention today?

The application should be optimised for frequent use from a mobile device, with a simple interface and fast access to the information that matters for squad decisions.

---

## 3. Scope

### In scope

* Player catalogue and master data.
* Historical player prices.
* FutbolFantasy price-trend classification.
* Personal player tracking.
* Release clause and clause-release date.
* Player notes.
* REST API backed by PostgreSQL.
* Angular PWA frontend.
* Automated testing and CI.
* Future automated data acquisition.
* Future statistical analysis and opportunity ranking.
* Future AWS deployment.

### Initially out of scope

* User authentication and multi-user accounts.
* Direct replication of FutbolFantasy functionality.
* Scraping as part of the first MVP.
* Advanced analytics before reliable historical data is available.
* Native Android/iOS applications.

---

## 4. Guiding Principles

1. **Data first.** Reliable historical data is more important than sophisticated analytics.
2. **Simple domain model.** Keep player identity, market history and personal tracking conceptually separate.
3. **Mobile first.** The main user experience should work well on a phone.
4. **Automation where it adds value.** Testing, data collection and deployment should progressively become automated.
5. **Evolvable architecture.** Start simple, but avoid decisions that make the later AWS and analytics phases unnecessarily difficult.
6. **Traceability.** Important architectural and product decisions should be documented.

---

## 5. Target Architecture

The target solution is:

* **Frontend:** Angular + TypeScript, delivered as a PWA.
* **Backend:** Java 21 + Spring Boot REST API.
* **Database:** PostgreSQL.
* **Database migrations:** Flyway.
* **Local development:** Docker Compose.
* **CI:** GitHub Actions.
* **Future AWS:** Amplify Hosting, API Gateway, Lambda, RDS and EventBridge as appropriate.

---

## 6. Definition of Success

The MVP is successful when a user can:

1. Open the application on a mobile device.
2. Browse players.
3. See current and historical prices.
4. See the recorded FutbolFantasy trend.
5. Mark players as watched, owned or discarded.
6. Store and review a release clause and notes.
7. Use the application reliably through the frontend without needing direct database access.

Later phases will add automated data acquisition and analytical features on top of this foundation.
