# Étape 1: Utiliser une image de base pour la compilation du projet
FROM maven:3.8.7-openjdk-17 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier pom.xml pour que Maven télécharge les dépendances
# Ceci permet de mettre en cache les dépendances et d'accélérer les builds suivants
COPY pom.xml .

# Télécharger les dépendances
RUN mvn dependency:go-offline

# Copier tout le code source du projet
COPY src ./src

# Compiler le projet en un fichier JAR exécutable
RUN mvn package -DskipTests

# Étape 2: Utiliser une image plus légère pour l'exécution de l'application
# L'image 'openjdk' est plus légère que l'image 'maven'
FROM openjdk:17-jdk-slim

# Exposer le port par défaut de Spring Boot
EXPOSE 8080

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR compilé depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Commande pour démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]