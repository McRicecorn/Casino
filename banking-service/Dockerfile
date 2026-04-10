# 1. Basis: Ein Image, das bereits Java 21 installiert hat
FROM eclipse-temurin:21-jdk-alpine

# 2. Ein Arbeitsverzeichnis im Container erstellen
WORKDIR /app

# 3. Die fertige Programmdatei (JAR) in den Container kopieren
COPY target/*.jar app.jar

EXPOSE 8080

# 4. Befehl zum Starten der Anwendung
ENTRYPOINT ["java", "-jar", "app.jar"]