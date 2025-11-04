# --- Build the application ---
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- Run the application ---
FROM eclipse-temurin:21-jdk
COPY --from=build app/target/AgriBora-0.0.1-SNAPSHOT.jar AgriBora.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "AgriBora.jar"]
