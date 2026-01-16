# --- Build Stage ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew build.gradle settings.gradle /app/
COPY gradle /app/gradle

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

COPY src /app/src
RUN ./gradlew bootJar --no-daemon

# --- Runtime Stage ---
FROM eclipse-temurin:23-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
