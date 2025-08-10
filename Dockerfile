# Étape 1 : Construction de l'application (le "builder")
# Utilise une image officielle OpenJDK 21, version slim pour un build plus léger.
FROM openjdk:21-jdk-slim AS builder

# Définit le répertoire de travail à l'intérieur du conteneur.
WORKDIR /app

# Copie le fichier POM pour télécharger les dépendances en premier.
COPY pom.xml .

# Télécharge les dépendances Maven.
RUN mvn dependency:go-offline

# Copie tout le code source.
COPY src ./src

# Exécute le build Maven pour compiler le code et créer le JAR.
# 'clean install' inclut l'exécution des tests unitaires.
RUN mvn clean install

# Étape 2 : Exécution de l'application (le "runtime")
# Utilise l'image de base Alpine qui est très petite et sécurisée.
FROM alpine:3.18

# Installe le JRE OpenJDK 21 manuellement pour garantir sa présence.
RUN apk add --no-cache openjdk21-jre

# Définit le répertoire de travail.
WORKDIR /app

# Copie le fichier JAR de l'étape de construction vers cette nouvelle image.
COPY --from=builder /app/target/BC_alternance-0.0.1-SNAPSHOT.jar BC_alternance.jar

# Expose le port de l'application.
EXPOSE 8080

# Commande pour démarrer l'application.
CMD ["java", "-jar", "BC_alternance.jar"]