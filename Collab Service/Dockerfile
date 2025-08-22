# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Install docker client (to allow the service to exec docker on host via mounted socket)
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
       ca-certificates curl gnupg \
       docker.io \
    && rm -rf /var/lib/apt/lists/*

# Copy Maven wrapper and pom.xml files
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Package the application (skip tests for faster build in dev)
RUN ./mvnw clean package -DskipTests

# Expose the port your Spring Boot app runs on
EXPOSE 8092

# Run the Spring Boot application
CMD java -jar target/*.jar