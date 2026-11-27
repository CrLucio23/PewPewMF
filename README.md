# PewPewMF

Piattaforma web dedicata al mondo del **softair e delle attività outdoor**, progettata per offrire un'esperienza interattiva di esplorazione dell'equipaggiamento, e-commerce di attrezzatura tattica, prenotazione di sessioni di gioco (con o senza noleggio), gestione partner/eventi e supporto tecnico tramite ticket.

---

## Funzionalità principali

* **Hero animata e Soldato Interattivo:** schermata iniziale animata con accesso all'armeria e figura dell'operatore softair navigabile per zone del corpo (`TESTA`, `BUSTO`, `ARMI`, `GAMBE`, `ACCESSORI`) con zoom dinamico e filtro rapido sugli articoli collegati.
* **Catalogo e Ricerca:** consultazione delle schede prodotto con dettagli tecnici, prezzi di acquisto, tariffe di noleggio, disponibilità e filtri per zona del corpo, partner o parola chiave.
* **Carrello e Ordini:** gestione del carrello in sessione, verifica delle giacenze, checkout con generazione ordine e storico acquisti nell'area personale.
* **Prenotazione Sessioni di Gioco:** calendario delle sessioni disponibili con configuratore dinamico per prenotare con la propria attrezzatura oppure includere il noleggio dell'equipaggiamento necessario.
* **Partner ed Eventi:** vetrina dei partner ufficiali, team collaboratori e operazioni/eventi speciali collegati ad attrezzature e sessioni di gioco.
* **Supporto Tecnico tramite Ticket:** apertura di richieste di assistenza da parte degli utenti e console dedicata allo staff di supporto per gestire, rispondere e chiudere i ticket.
* **Area Account e Gestione Ruoli:** registrazione, autenticazione e dashboard personale con ordini, prenotazioni e ticket, oltre ai permessi differenziati per clienti, supporto e amministratori.

---

## Stack tecnologico

* **Back-end:** Java 21, Spring Boot 3 (Spring Web, Spring Data JPA, Spring Security, Spring Validation)
* **Front-end:** Thymeleaf, HTML5, CSS3, JavaScript
* **Database:** PostgreSQL
* **Build & Dependency Management:** Apache Maven

---

## Struttura del progetto

Il codice sorgente applicativo è organizzato sotto `src/main/java/it/pewpewmf/platform`:

```text
src/
├── main/
│   ├── java/it/pewpewmf/platform/
│   │   ├── config/        # Configurazione Spring Security e DataSeeder iniziale
│   │   ├── controller/    # Controller web e API REST per l'interfaccia utente
│   │   ├── dto/           # Data Transfer Object per scambio dati leggero
│   │   ├── model/         # Entità JPA ed Enum di dominio
│   │   ├── repository/    # Interfacce Spring Data JPA per accesso al DB
│   │   ├── service/       # Logica applicativa dei sottosistemi
│   │   └── PewpewMfApplication.java
│   └── resources/
│       ├── static/        # Fogli di stile CSS, script JS e immagini
│       ├── templates/     # Template HTML Thymeleaf e fragment condivisi
│       └── application.properties
