# ---- STAGE 1: Build ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy toàn bộ source code
COPY . .

# Build project và skip test
RUN mvn clean package -DskipTests

# ---- STAGE 2: Run ----
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/*.war app.war
ENTRYPOINT ["java", "-jar", "app.war"]

