# BeanEMS Backend

Spring Boot 2.7.0 / Java 17 / Maven backend for BeanEMS, backed by MySQL via Spring Data JPA.

## Deployment

This backend runs in production on a single-instance AWS Elastic Beanstalk environment
(`beanems-env`, Corretto 17 on Amazon Linux 2023), fronted by CloudFront for HTTPS, with a
private RDS MySQL instance (`beanems-prod`) reachable only from the EB environment's security
group. The frontend (Vercel, `BeanEMS` repo) calls this backend through CloudFront.

For the full architecture diagram, environment variable reference, redeploy commands, and
security notes, see [DEPLOYMENT.md in the BeanEMS (frontend) repo](https://github.com/sureshkandimalla/BeanEMS/blob/master/DEPLOYMENT.md).

### Quick facts

- **API base URL (production):** `https://d3bakmjfonjh6u.cloudfront.net/api/v1`
- **EB application / environment:** `beanems-backend` / `beanems-env` (region `us-east-2`)
- **Local build gotcha:** Lombok 1.18.26 doesn't run correctly under JDK 23 (Homebrew's default
  `mvn` JVM) — build with `JAVA_HOME` pointed at an installed JDK 19 or 17 instead:
  ```bash
  JAVA_HOME=/path/to/jdk-19-or-17 mvn clean package -DskipTests
  ```
- **Config is environment-variable driven** (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`,
  `SERVER_PORT`, `CORS_ALLOWED_ORIGINS`) with the previous hardcoded local values kept as
  defaults in `application.properties`, so the same jar runs locally and in production
  unchanged.
- **`Procfile`** (`web: java -jar application.jar`) is what Elastic Beanstalk's Java SE platform
  uses to start the app — `application.jar` itself is gitignored and rebuilt fresh for each
  deploy.
