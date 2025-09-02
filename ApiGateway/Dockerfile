# Multi-stage build for smaller production image
FROM openjdk:21-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml files
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Package the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Production stage
FROM openjdk:21-jre-slim

WORKDIR /app

# Install curl for health checks
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Copy the jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8086

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8086/actuator/health || exit 1

# Run the Spring Boot application with prod profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]