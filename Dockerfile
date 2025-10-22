# ---- STAGE 1: Build ----
FROM maven:3-openjdk-17 AS build
WORKDIR /app

# Copy toàn bộ source code
COPY . .

# Build project và skip test
RUN mvn clean package -DskipTests

# ---- STAGE 2: Run ----
FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/X-Store-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]

