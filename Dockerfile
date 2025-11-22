# --- Build Stage ---
FROM gradle:8.10-jdk-25 AS build
WORKDIR /app

# Copy Gradle config first for caching
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source code
COPY src /app/src

# Build Spring Boot JAR
RUN ./gradlew bootJar --no-daemon

# --- Runtime Stage ---
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copy built jar from builder stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
