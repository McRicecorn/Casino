# Online Casino - Microservice Architektur
Dieses Projekt wurde im Rahmen des Moduls B42 Softwareengineering und Softwarearchitekturen (WS 25/26) entwickelt. Ziel ist die Abbildung eines Online-Casinos mittels einer RESTful Microservice-Architektur.

# 📝 Projektbeschreibung
Das System ermöglicht es Benutzern, Konten zu erstellen, Guthaben zu verwalten und an verschiedenen Casinospielen teilzunehmen. Ein besonderer Fokus liegt auf der transparenten Darstellung von Gewinnchancen und Statistiken.

## Die Microservices:
### 🏦 Banking-Service:

Zuständig für User-Management, Transaktionen und Statistiken.

Architektur: Vertical Slice (Unterteilung in User, Transaction und Stats).

Anmerkung zur Testcoverage: Die BankingServiceApplication wurde nicht getestet, da sie keine Geschäftslogik enthält und nur Spring-Boot startet.

### 🛞 Roulette-Service:

Modellierung des Spiels Roulette.

Architektur: Geschichtete Architektur (Layered).

Anmerkung zur Testcoverage: Die RouletteServiceApplication wurde nicht getestet, da sie keine eigene Logik enthält und nur den Spring-Boot-Start ausführt.
Im RouletteGameService sind außerdem nicht alle möglichen Fälle vollständig getestet, da es sehr viele Wettkombinationen gibt und zusätzliche Tests hier keinen wirklichen Mehrwert gebracht hätten.

### 🎰 Slotmachine-Service:

Modellierung einer klassischen Slotmachine.

Architektur: Geschichtete Architektur (Layered) & MVC-Architektur.
  
  
Anmerkung zur Testcoverage: Unter dem IntelliJ-Runner werden einige Methoden nicht erreicht, daher sollte in den Einstellungen auf JaCoCo umgestellt werden.
Dem Slotmachine Handler fehlen zwei Branches für die Coverage in Zeile 155. Hier wurde darauf verzichtet, für jede mögliche Walzenkombination den kleinen Gewinn zu testen.
Ebenso wurde die SlotmachineApplication-Klasse nicht getestet, da sie keine Geschäftslogik enthält und lediglich Springboot testen würde.

Jeder Service verfügt über eine eigene PostgreSQL-Datenbank, um eine vollständige Entkopplung zu gewährleisten.

# 🛠 Technologien
Java 21 & Spring Boot

Maven (Multi-Module Projekt)

PostgreSQL (Datenhaltung)

Docker & Docker Compose (Containerisierung)

Swagger (OpenAPI 3) (API-Dokumentation)

JUnit & Mockito (Testing)

# 🚀 Installation & Setup
Voraussetzungen
Docker & Docker Compose installiert

Java 21 JDK

Maven

## Schritt-für-Schritt Anleitung
Repository klonen:

Bash
```
git clone https://github.com/McRicecorn/Casino.git
```
  
Artefakte bauen (JAR-Dateien):
Führe im Hauptverzeichnis aus:

Bash
```
mvn clean install -DskipTests
```

Container starten:
Stelle sicher, dass die docker-compose.yml im Hauptverzeichnis liegt und führe aus:

Bash
```
docker-compose up --build
```

Dies startet alle 3 Microservices sowie die 3 zugehörigen Datenbanken (insgesamt 6 Container).

# 🎮 Benutzung
Sobald die Container laufen, können die APIs über die integrierten Swagger-UI Oberflächen getestet werden:

Banking-Service: http://localhost:8080/swagger-ui.html

Roulette-Service: http://localhost:8081/swagger-ui.html

Slotmachine-Service: http://localhost:8082/swagger-ui.html


# ⚖️ Lizenz
Dieses Werk ist lizenziert unter einer Creative Commons Namensnennung 4.0 International Lizenz (CC BY 4.0).

Weitere Informationen finden Sie unter: http://creativecommons.org/licenses/by/4.0/

# 👥 Autoren
Catharina Hoppensack, Matr.-Nr.: 594129  
Duc....  
Elias Märker, Matr.-Nr.: 594298  