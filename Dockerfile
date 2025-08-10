# Étape 1 : Construction de l'application (le "builder")
# Utilise une image avec Java 17 et Maven.
FROM maven:3.8.5-openjdk-17 AS builder

# Définit le répertoire de travail à l'intérieur du conteneur.
WORKDIR /app

# Copie le fichier POM pour télécharger les dépendances en premier.
# Cela optimise le cache Docker si le POM ne change pas.
COPY pom.xml .

# Télécharge les dépendances Maven.
RUN mvn dependency:go-offline

# Copie tout le code source.
COPY src ./src

# Exécute le build Maven pour compiler le code et créer le JAR.
# Le test est exécuté ici. Si vous avez besoin d'ignorer les tests dans le Dockerfile
# pour un build plus rapide, vous pouvez ajouter -DskipTests.
RUN mvn clean install

# Étape 2 : Exécution de l'application (le "runtime")
# Utilise une image plus petite avec uniquement le JRE (Java Runtime Environment).
FROM eclipse-temurin:17-jre-focal

# Définit le répertoire de travail.
WORKDIR /app

# Copie le fichier JAR de l'étape de construction vers cette nouvelle image.
# L'argument '--from=builder' indique de prendre le fichier de l'étape nommée 'builder'.
COPY --from=builder /app/target/BC_alternance-0.0.1-SNAPSHOT.jar BC_alternance.jar

# Expose le port de l'application.
EXPOSE 8080

# Commande pour démarrer l'application.
# 'CMD' est la commande qui sera exécutée lorsque le conteneur démarrera.
# Elle ne se termine pas, ce qui est le comportement attendu pour une application web.
CMD ["java", "-jar", "BC_alternance.jar"]
