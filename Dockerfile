# ==============================================================================
# Multi-Stage Dockerfile for Automated Microinsurance Enrollment Platform
# Stage 1: Build stage with OpenJDK 21 & Maven
# Stage 2: Minimal, secure JRE 21 runtime
# ==============================================================================

# Stage 1: Builder
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Cache Maven dependencies layer
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and package
COPY src ./src
RUN mvn clean package -DskipTests -B

# ==============================================================================
# Stage 2: Production Runtime
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Metadata
LABEL maintainer="DevOps Team <devops@microinsurance.org>"
LABEL version="1.0.0"
LABEL description="Microinsurance Enrollment Platform Container"

# Create unprivileged system group and user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy the built artifact from builder
COPY --from=builder /build/target/microinsurance-enrollment-platform-1.0.0.jar app.jar

# Set permissions
RUN chown -R appuser:appgroup /app
USER appuser

# Expose port
EXPOSE 8080

# Environment variables with sensible defaults
ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080 \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Healthcheck probe against the health endpoint
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/api/health || exit 1

# Launch the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
