# ---------- Stage 1: Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven configuration first (for caching dependencies)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy a specific JAR from the build folder
COPY target/ChocoWebClient-0.0.1-SNAPSHOT.jar app.jar

# Expose port if your app is a web server
EXPOSE 8080

# Default command to run your Java app
ENTRYPOINT ["java", "-jar", "app.jar"]
