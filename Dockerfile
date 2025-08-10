# Étape 1 : build
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean install -DskipTests

# Étape 2 : runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/BC_alternance-0.0.1-SNAPSHOT.jar BC_alternance.jar
EXPOSE 8080
CMD ["java", "-jar", "BC_alternance.jar"]
