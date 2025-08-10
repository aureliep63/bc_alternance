# Étape 1 : Construction de l'application (le "builder")
# Utilise une image avec Java 21 et Maven.
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Définit le répertoire de travail à l'intérieur du conteneur.
WORKDIR /app

# Copie le fichier POM pour télécharger les dépendances en premier.
COPY pom.xml .

# Télécharge les dépendances Maven.
RUN mvn dependency:go-offline

# Copie tout le code source.
COPY src ./src

# Exécute le build Maven pour compiler le code et créer le JAR.
# 'clean install' peut prendre du temps, si vous rencontrez des problèmes de timeout, vous pouvez ajouter -DskipTests
RUN mvn clean install

# Étape 2 : Exécution de l'application (le "runtime")
# Utilise une image plus petite avec uniquement le JRE (Java Runtime Environment) 21.
FROM eclipse-temurin:21-jre-focal

# Définit le répertoire de travail.
WORKDIR /app

# Copie le fichier JAR de l'étape de construction vers cette nouvelle image.
COPY --from=builder /app/target/BC_alternance-0.0.1-SNAPSHOT.jar BC_alternance.jar

# Expose le port de l'application.
EXPOSE 8080

# Commande pour démarrer l'application.
CMD ["java", "-jar", "BC_alternance.jar"]
