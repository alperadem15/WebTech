#Autovermietung


Dieses Projekt ist eine einfache Spring Boot Anwendung zur Verwaltung von vermieteten Autos.

- `GET /cars` → gibt eine Liste aller Autos zurück

##Projektstruktur

- `com.autovermietung.Car` → Modellklasse für Autos  
- `com.autovermietung.web.CarController` → REST-Controller für Endpunkte  
- `AutovermietungApplication.java` → Main-Klasse zum Starten der Spring Boot App

Autovermietungsplattform

Dieses Projekt ist eine Webanwendung zur Autovermietung, bei der es zwei unterschiedliche Benutzerrollen gibt: Kunden und Vermieter.
Die Anwendung ermöglicht es Vermietern, Autos anzubieten und ihren Umsatz einzusehen, während Kunden Autos verschiedener Vermieter durchsuchen und mieten können.

Tech-Stack:
Spring, Vue und Postgres

Frontend: Web-Oberfläche
Deployed als Static Site auf Render

Backend: REST-API
Deployed als Web Service auf Render

Datenbank: PostgreSQL
auf Render gehostet

Benutzerrollen:

Kunde
    Ein Kunde kann:
        - Ein Kundenkonto erstellen 
        - Sich einloggen 
        - Alle verfügbaren Autos aller Vermieter sehen 
    Pro Auto folgende Informationen einsehen:
        - Marke 
        - Modell 
        - Preis 
        - Firmenname des Vermieters 
        - Genau ein Auto mieten 
        - Sein aktuell gemietetes Auto unter „Meine Miete“ einsehen
    Über das Burger-Menü:
        - Zur Startseite (Kundenansicht) wechseln 
        - Seine Miete einsehen 
        - Sich ausloggen

Vermieter
    Ein Vermieter kann:
        - Ein Vermieterkonto erstellen
        - Sich als Vermieter einloggen 
        - Eigene Autos erstellen, bearbeiten und löschen 
        - Nur seine eigenen Autos sehen
        - Keine Autos anderer Vermieter sehen oder verändern 
        - Den Umsatz einsehen, der durch Vermietungen seiner Autos entsteht
        - Beliebig viele Autos einstellen und vermieten.
    Über das Burger-Menü:
        - Zum Dashboard wechseln
        - Den Umsatz einsehen
        - Sich ausloggen


Zugriff & Sicherheit

Kunden und Vermieter haben getrennte Logins
Rollenbasierte Zugriffskontrolle:
Vermieter können nur ihre eigenen Autos verwalten
Kunden können keine Autos bearbeiten oder löschen
Der Umsatz eines Vermieters wird automatisch erhöht, sobald ein Kunde eines seiner Autos mietet

Passwörter werden nicht als Klartext gespeichert. Dafür wird implementation 'org.springframework.security:spring-security-crypto' verwendet.


📋 Use-Cases (Auswahl)

1. Registrierung als Kunde oder Vermieter

2. Login als Kunde oder Vermieter

3. Auto erstellen / löschen (nur Vermieter)

4. Autos durchsuchen und anzeigen (Kunde)

5. Auto mieten (Kunde)

6. Umsatzberechnung für Vermieter

7. Logout über Burger-Menü

8. Kunde: Zwischen Kundenansicht (Home) und Meine Miete wechseln über Burger-Menü 
   Vermieter: Zwischen Vermieter-Dashboard und Umsatz wechseln über Burger-Menü