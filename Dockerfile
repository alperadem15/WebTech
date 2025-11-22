# --- Build Stage ---
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Download dependencies
RUN chmod +x gradlew
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

ENTRYPOINT ["java", "-jar", "app.jar"]
