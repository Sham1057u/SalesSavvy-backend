# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project and skip tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Run the application using JRE
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/salessavvy-0.0.1-SNAPSHOT.jar app.jar

# Expose port your app runs on (8080 usually)
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
