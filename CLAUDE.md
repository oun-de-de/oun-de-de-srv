# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
mvn clean install
mvn package

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=JwtServiceTest

# Run a single test method
mvn test -Dtest=JwtServiceTest#methodName

# Code style check
mvn checkstyle:check

# Database migrations (requires DB env vars set)
mvn flyway:migrate

# Run application
mvn spring-boot:run
```

**Required environment variables:**
- `DB_URL` — JDBC connection URL (MySQL)
- `DB_USRNAME` — database username
- `DB_PASSWORD` — database password
- `JWT_SECRET` — secret key for JWT signing

## Architecture

This is a Spring Boot 3.5.9 mini-ERP for a truck weighing station business. Core domains: customers, vehicles, coupons (weighing sessions), invoices, loans, inventory, accounting, cash transactions.

**Layer structure:** `controllers` → `services` → `repositories` → `entities`
**DTOs** live under `controllers/dto/` organized by feature.
**MapStruct mappers** handle entity ↔ DTO conversion.
**Exceptions** bubble up to `handlers/GlobalExceptionHandler` which produces standardized error responses.

### Multi-tenancy

All business entities extend `OrgManaged`, which adds `org_id` and a Hibernate `@Filter` (`orgFilter`). Every query is automatically scoped to the current organization. When writing new repositories or queries, this filter is applied transparently as long as entities extend `OrgManaged`.

### Authentication

JWT-based auth (`JwtAuthFilter` → `JwtService`). Tokens expire in 1 hour, refresh tokens in 24 hours. Public routes: `/api/v1/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/actuator/health`.

### Database migrations

55 versioned Flyway migrations in `src/main/resources/db/migration/` (V1–V55). Always add new migrations as the next version; never modify existing ones. `ddl-auto: none` — schema is entirely managed by Flyway.

### Kafka / Avro

Kafka integration is partially implemented (alert messaging). Avro schema at `src/main/resources/alert.avsc`. The `OunDeDeApplication` main class currently has `SpringApplication.run` commented out while Kafka wiring is in progress — restore it when the integration is complete.

### Packages under `com.cdtphuhoi.oun_de_de`

| Package | Purpose |
|---|---|
| `controllers/` | 15 REST controllers, base path `/api/v1/` |
| `services/` | Business logic; `auth/` subpackage for JWT/user auth |
| `repositories/` | Spring Data JPA repositories (40+) |
| `entities/` | JPA entities (30); most extend `OrgManaged` |
| `configs/` | `WebSecurityConfig`, `SwaggerConfig`, `ApplicationConfig` |
| `filters/` | `JwtAuthFilter` — validates Bearer tokens on every request |
| `handlers/` | `GlobalExceptionHandler` — maps exceptions to HTTP responses |
| `mappers/` | MapStruct interfaces for DTO/entity conversion |
| `validators/` | Custom constraint validators |
| `jobs/` | Scheduled tasks (e.g., monthly balance snapshots) |
| `aop/` | Cross-cutting concerns (logging, auditing) |
| `common/` | Enums, constants, shared value lists |
| `exceptions/` | `BadRequestException`, `ResourceNotFoundException`, etc. |

### Note on `com.kakfainaction` package

Separate top-level package for Kafka alert infrastructure (Alert, AlertKeySerde, AlertLevelPartitioner, AlertCallback). Not yet integrated with the main Spring Boot app.

## API Documentation

Swagger UI available at `/swagger-ui.html` when running locally. Full request/response specs for the coupon creation flow are documented in `api-integration-spec.md`.
