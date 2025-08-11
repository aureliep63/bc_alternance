# Étape 1 : Builder l'application
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests


# Étape 2 : Image finale légère
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/BC_alternance-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
