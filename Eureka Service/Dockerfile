# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml files
COPY mvnw* pom.xml ./
COPY .mvn .mvn

# Copy the source code
COPY src ./src

# Package the application (skip tests for faster build in dev)
RUN ./mvnw clean package -DskipTests

# Expose the port your Spring Boot app runs on (change as needed)
EXPOSE 8761

# Run the Spring Boot application
CMD java -jar target/*.jar