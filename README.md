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

2. Levantar DB, backend y frontend con Docker:
   - Asegúrate de tener Docker y Docker Compose
   - Ejecuta: docker compose up --build

3. Ejecutar la app Spring Boot (si prefieres local sin contenedor):
   - mvn -f backend/pom.xml spring-boot:run (i.e. mvn backend/pom.xml spring-boot:run "-Dspring-boot.run.arguments=--DB_URL=jdbc:postgresql://localhost:5432/fantasy" )

4. Angular (carpeta frontend/):
   - cd frontend
   - npm install
   - ng serve (proxy configurado hacia el backend en localhost:8080)

## Estructura del proyecto

- backend/: aplicación Spring Boot (entidades, repositorios, controladores, tests)
- backend/src/main/resources/db/migration/: migraciones Flyway
- frontend/: aplicación Angular (PWA, standalone components, Angular Material)
- docker-compose.yml: Postgres + backend + frontend

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

- Fase 0: Fundación del proyecto ✅
- Fase 1: API básica y modelo de datos ✅
- Fase 2: Frontend Angular (en curso)
- Fase 3: Adquisición de datos externos
- Fase 4: Análisis y detección de oportunidades
- Fase 5: Despliegue en AWS (RDS, Lambda, API Gateway, Amplify)
- Fase 6: Funcionalidades avanzadas

Ver `docs/03-roadmap.md` para el detalle completo de fases y criterios de salida.

## Contribución

- Este repo es tu entorno personal; ajusta lo que necesites. Si quieres que empuje los ficheros iniciales al repo, dime la rama.
