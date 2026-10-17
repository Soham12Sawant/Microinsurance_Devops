# Milestone 11: Docker Image and Container Lifecycle
## Automated Microinsurance Enrollment Platform

---

### 1. Multi-Stage Dockerfile Architecture
The `Dockerfile` employs multi-stage caching to produce a lightweight, secure production image:

```dockerfile
# Stage 1: Build stage with OpenJDK 21 & Maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Production Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime

LABEL maintainer="DevOps Team <devops@microinsurance.org>"
LABEL version="1.0.0"

# Unprivileged system user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

COPY --from=builder /build/target/microinsurance-enrollment-platform-1.0.0.jar app.jar
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/api/health || exit 1

ENTRYPOINT ["sh", "-c", "java -Xms256m -Xmx512m -jar app.jar"]
```

---

### 2. Complete Container Lifecycle Command Runbook

#### 2.1 Build & Tag Image:
```bash
docker build -t microinsurance-platform:1.0.0 -t microinsurance-platform:latest .
```

#### 2.2 Verify Image in Local Registry:
```bash
docker images | grep microinsurance-platform
# Output:
# microinsurance-platform   1.0.0     a7d9f21b3c4e   2 minutes ago   218MB
# microinsurance-platform   latest    a7d9f21b3c4e   2 minutes ago   218MB
```

#### 2.3 Run Container with Port Mapping:
```bash
docker run -d --name microinsurance-app \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  microinsurance-platform:1.0.0
```

#### 2.4 Inspect Container Status & Port Binding:
```bash
docker ps --filter "name=microinsurance-app"
# Output:
# CONTAINER ID   IMAGE                          COMMAND                  STATUS                   PORTS                    NAMES
# 8f192b03c94a   microinsurance-platform:1.0.0  "sh -c 'java -Xms256…"   Up 45 seconds (healthy)  0.0.0.0:8080->8080/tcp   microinsurance-app
```

#### 2.5 Inspect Container Logs:
```bash
docker logs -f --tail 50 microinsurance-app
# Output:
# 2026-10-17 19:50:04.021 INFO  [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port 8080 (http)
# 2026-10-17 19:50:04.032 INFO  [main] c.m.p.MicroinsuranceApplication         : Started MicroinsuranceApplication in 3.84 seconds
```

#### 2.6 Health Check Verification:
```bash
docker inspect --format='{{json .State.Health.Status}}' microinsurance-app
# Output:
# "healthy"
```

#### 2.7 Stop, Restart, and Remove Lifecycle:
```bash
# Restart container
docker restart microinsurance-app

# Graceful stop
docker stop microinsurance-app

# Remove container
docker rm microinsurance-app
```
