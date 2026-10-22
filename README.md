# PewPewMF

Progetto universitario di Ingegneria del Software: shop di attrezzatura softair e
prenotazione delle giocate. I mockup di riferimento sono in `Esempio/`.

Questo primo lotto contiene soltanto il **catalogo dimostrativo**: cinque prodotti,
ricerca per nome/marca/codice, filtri per categoria/marca/prezzo, ordinamento e stato
senza risultati. La JSP mostra i prodotti anche senza JavaScript; in quel caso i
filtri restano disabilitati. I disegni dei prodotti sono segnaposto vettoriali.

## Tecnologie e avvio

- JDK 17 o successivo; il bytecode viene compilato per Java 17.
- Apache Tomcat **11.0.x**, Jakarta Servlet 6.1, JSP e JSTL 3.
- Maven 3.9.x; HTML, CSS e JavaScript senza framework frontend.

Tomcat 11 usa il namespace `jakarta.servlet`: questo progetto non è configurato per
Tomcat 9 (`javax.servlet`). Riferimenti: [Tomcat 11](https://tomcat.apache.org/tomcat-11.0-doc/index.html)
e [Jakarta Tags / JSTL](https://jakarta.ee/specifications/tags/3.0/).

1. Importare `pom.xml` nell'IDE come progetto Maven.
2. Configurare il JDK e un runtime Tomcat 11 nell'IDE.
3. Eseguire dalla cartella del progetto:

   ```sh
   mvn clean package
   ```

4. Distribuire `target/pewpewmf.war` nella cartella `webapps` del proprio Tomcat,
   oppure configurare nell'IDE il deployment del WAR con context path `/pewpewmf`.
5. Avviare Tomcat e aprire `http://localhost:8080/pewpewmf/catalogo`.
   Anche `http://localhost:8080/pewpewmf/` apre il catalogo.

Porta e context path possono cambiare con la configurazione dell'IDE. Non è necessario
un database per questo primo lotto. Le dipendenze vengono scaricate da Maven al primo build.

## Struttura MVC

```text
Browser → CatalogoServlet → ProdottoDAO → Prodotto
                  ↓
        WEB-INF/views/catalogo.jsp
                  ↓
          CSS + JavaScript
```

- `model/Prodotto.java`: dati immutabili, validazione dei valori e prezzi `BigDecimal`.
- `dao/ProdottoDAO.java`: accesso di sola lettura a dati demo in memoria, con
  `findAll()` e `findById()`. **Non implementa ancora persistenza JDBC.**
- `controller/CatalogoServlet.java`: carica dati e opzioni dei filtri, poi inoltra
  la richiesta alla JSP. Le rotte usano `@WebServlet`, quindi non serve `web.xml`.
- `WEB-INF/views/catalogo.jsp`: presentazione con EL/JSTL, senza scriptlet Java;
  non è raggiungibile direttamente dal browser.
- `assets/`: stile ispirato a `Esempio/mu2.png` e filtri eseguiti sui prodotti già
  caricati. Con un catalogo più grande ricerca e paginazione passeranno al DAO.

## Primo lotto: file da rivedere e suddivisione proposta

Nove file interessati: otto nuovi e questo README aggiornato, tre per persona.
La suddivisione indica le responsabilità di revisione e i successivi commit; non
modifica gli autori Git. Nessun commit è incluso in questo lotto.

| Persona | File | Responsabilità |
| --- | --- | --- |
| Lucio / CrLucio23 | `pom.xml`, `.gitignore`, `README.md` | Configurazione, esclusioni dei file locali, documentazione |
| giongio | `src/main/java/it/pewpewmf/model/Prodotto.java`, `src/main/java/it/pewpewmf/dao/ProdottoDAO.java`, `src/main/java/it/pewpewmf/controller/CatalogoServlet.java` | Model, accesso ai dati e controller |
| SweatLife 5 | `src/main/webapp/WEB-INF/views/catalogo.jsp`, `src/main/webapp/assets/css/catalogo.css`, `src/main/webapp/assets/js/catalogo.js` | Vista, stile e interazioni |

I passaggi sono separabili per scopo: configurazione Maven; esclusioni Git;
documentazione; modello prodotto; DAO demo; controller catalogo; vista JSP;
stile responsive; filtri e ordinamento JavaScript. Ogni commit dovrebbe descrivere
la modifica effettivamente revisionata, mantenendo le dipendenze in questo ordine.

## Verifiche manuali

- Il catalogo mostra cinque prodotti, prezzi in euro e un prodotto esaurito.
- Cercare `bolle` trova la marca `Bollé`; ricerca, categorie, marca e prezzo si combinano.
- Selezionare più categorie mantiene i prodotti di ciascuna categoria selezionata.
- Una ricerca senza corrispondenze mostra lo stato vuoto; «Mostra tutti i prodotti»
  ripristina i filtri, l'ordine iniziale e il conteggio.
- Verificare ordinamento crescente/decrescente, navigazione da tastiera e layout mobile.
- L'accesso diretto a `/WEB-INF/views/catalogo.jsp` deve restituire 404.

## Lotti successivi, da sviluppare separatamente

1. Scelta del database, schema SQL, connessione JDBC e DAO persistente.
2. Pagina prodotto e carrello in sessione, con controllo di quantità e disponibilità.
3. Utenti, autenticazione e gestione degli ordini.
4. Eventi, disponibilità separate di posti e kit, prenotazione delle giocate.
5. Home, pagina campo e immagini definitive, ispirate agli altri mockup.
6. Test automatici, documentazione richiesta dal corso e preparazione della consegna.

Prodotti, prezzi e disponibilità sono dati dimostrativi. Checkout, pagamenti,
account e prenotazioni non sono implementati in questo lotto.
