# fantasy-tracker

Aplicación personal para realizar seguimiento de jugadores de Fantasy: precios, cláusulas y oportunidades.

## Resumen

- Frontend: Angular PWA
- Backend: Java 21 + Spring Boot
- DB: PostgreSQL (Flyway para migraciones)
- Hosting: AWS (futura integración)

## Quickstart (desarrollo local)

1. Clonar:

   git clone https://github.com/XavierCalvo/fantasy-tracker.git

2. Levantar DB y backend con Docker:
   - Asegúrate de tener Docker y Docker Compose
   - Ejecuta: docker-compose up --build

3. Ejecutar la app Spring Boot (si prefieres local sin contenedor):
   - mvn -f backend/pom.xml spring-boot:run

4. Angular: (carpeta frontend, aún por crear)
   - npm install
   - ng serve

## Estructura inicial incluida en este commit

- backend/: esqueleto Spring Boot con entidades, repositorios y controlador básico
- backend/src/main/resources/db/migration/V1__init.sql: migración inicial
- docker-compose.yml: Postgres + backend (conexión por variables)

## Endpoints básicos propuestos

- GET    /api/players
- GET    /api/players/{id}
- POST   /api/players
- PUT    /api/players/{id}
- DELETE /api/players/{id}
- GET    /api/players/{id}/prices
- POST   /api/players/{id}/prices
- GET    /api/players/{id}/tracking
- POST   /api/players/{id}/tracking
- PUT    /api/tracking/{id}
- DELETE /api/tracking/{id}

## Documentación de la API (Swagger)

Con el backend en marcha (local o vía Docker), la documentación interactiva está disponible en:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Roadmap

- Fase 1: API básica y modelo de datos
- Fase 2: Frontend Angular
- Fase 3: Despliegue en AWS (RDS, Lambda, API Gateway)
- Fase 4: Collector y automatización

## Contribución

- Este repo es tu entorno personal; ajusta lo que necesites. Si quieres que empuje los ficheros iniciales al repo, dime la rama.
