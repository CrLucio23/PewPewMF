# STEP RESTYLE: Rinnovamento Grafico Contemporaneo (Ispirato a `empi.re`)

> **Prerequisito:** Leggi `00_MASTER_RULES.md`.
> **Obiettivo Commit:** Eliminare ogni elemento visivo datato (bordi spessi stile anni 2000, sfondi marrone piatto su tutta la card, ombreggiature pesanti, font standard) e trasformare l'intera piattaforma **PEWPEW MF** in un sito moderno, pulito, ad alto impatto visivo ispirato all'estetica di **EMPIRE (`empi.re`)**: tipografia bold monumentale, griglie minimaliste edge-to-edge, schede prodotto pulite con micro-badge, hover fluidi e navbar trasparente/minimalista, **mantenendo intatta la logica Java/Spring/Thymeleaf, la Hero con video e lo Stage Interattivo con lo zoom sul Soldato**.
> **NON modificare:** Controller Java, Service, Entity, Repository o endpoint JSON esistenti.

---

## 1. Filosofia Visiva (EMPIRE Aesthetic + Palette PEWPEW MF)
Il sito `empi.re` comunica modernità grazie a **contrasto netto, tipografia oversize, zero fronzoli e micro-interazioni fluide**. Applichiamo questa direzione artistica usando esclusivamente i nostri colori:

- **Sfondo Primario (`--color-black-outline`: `#1A1A1A`):** nero profondo opaco su tutta la pagina, senza riquadri colorati invadenti.
- **Superfici Card & Pannelli (`#22201E` / `#4A1E1E` con opacità):** al posto dei vecchi box marroni con bordi gialli spessi, usa superfici quasi nere con bordi sottilissimi (`1px solid rgba(238, 217, 179, 0.12)`) e usa `--color-dark-mountain` (`#4A1E1E`) solo come accento di profondità o gradienti sottili.
- **Tipografia Moderna (Google Fonts via CDN in tutti i template):**
  - **Titoli & Display:** `'Syne'` o `'Space Grotesk'` (peso `700`/`800`, `text-transform: uppercase`, `letter-spacing: -0.03em`, interlinea compatta `0.95`).
  - **Testi & UI:** `'Inter'` o `'Plus Jakarta Sans'` (pulito, leggibile, moderno).
  - **Dettagli Tecnici / Prezzi / SKU:** `'JetBrains Mono'` (monospace tattico per prezzi in `#F08C00`, codici e coordinate zoom).
- **Border Radius:** Spigoli netti o quasi netti (`border-radius: 2px` o `4px` al massimo — niente bottoni tondeggianti stile Web 2.0).

---

## 2. Task 1: Navbar Minimalista Sticky (`templates/fragments/navbar.html`)
Ridisegna la navbar ispirandoti all'header pulito di `empi.re`:
1. **Struttura:**
   - Altezza compatta (`68px`), sfondo `rgba(26, 26, 26, 0.85)` con `backdrop-filter: blur(14px)`, bordo inferiore sottilissimo `1px solid rgba(238, 217, 179, 0.1)`.
   - **Sinistra:** Logo PEWPEW MF pulito + wordmark bold `PEWPEW MF` (senza sottotitoli superflui).
   - **Centro:** Voci di navigazione distanziate (`gap: 28px`), font `Inter` `0.82rem` uppercase con `letter-spacing: 0.12em`, colore `#EED9B3` con underline animata dal centro in `#C92A2A` al passaggio del mouse:
     - `ARMERIA` (`/`)
     - `CATALOGO` (`/catalogo`)
     - `SESSIONI` (`/prenotazioni`)
     - `PARTNER & EVENTI` (`/partner`)
   - **Destra (Azioni rapide iconiche & minimali):**
     - Pillola discreta `SUPPORTO 24/7` (`/ticket`) con puntino verde pulsante (`#4E5D3F`).
     - Link/Icona `CARRELLO (0)` (`/carrello`) in font monospace.
     - Pulsante `ACCEDI` / `ACCOUNT` (`/account` o `/login`): rettangolo netto con sfondo `#C92A2A` e testo `#EED9B3` uppercase bold.

---

## 3. Task 2: Home Page — Hero + Soldato Interattivo + Vetrine Stile EMPIRE (`templates/index.html`)
Mantieni **al 100%** la Hero con il video introduttivo e lo Stage Interattivo del Soldato con lo zoom sulle 5 zone (`TESTA`, `BUSTO`, `ARMI`, `GAMBE`, `ACCESSORI`), ma eleva il design dello stage e aggiungi subito sotto le sezioni moderne stile `empi.re`:

1. **Restyling Stage Soldato Interattivo:**
   - Elimina i vecchi riquadri pesanti attorno al soldato: il soldato (`soldato-kittato.jpg`) deve emergere direttamente sullo sfondo nero `#1A1A1A` con una sfumatura radiale morbida dietro di lui (`radial-gradient(circle, rgba(74,30,30,0.45) 0%, rgba(26,26,26,0) 70%)`).
   - **Hotspot Minimalisti:** Sostituisci i vecchi cerchi grossolani con marker stile mirino/radar sottili (`24px`, anello `1.5px solid #F08C00`, punto centrale `#C92A2A` con onda pulse silenziosa).
   - **Pannello Equipaggiamento (Destra):** Design glass/dark minimal (`background: rgba(26, 26, 26, 0.92)`, `border: 1px solid rgba(238, 217, 179, 0.15)`). Titolo zona gigante (`TESTA`, `ARMI`, ecc.), lista pulita degli articoli senza box annidati e pulsante full-width `#C92A2A` per aprire il catalogo filtrato.

2. **Sezione "TRENDING GEAR" (Subito sotto lo stage del soldato, stile vetrina `empi.re`):**
   - Intestazione moderna con titolo grande a sinistra (`EQUIPAGGIAMENTO IN EVIDENZA`) e link minimale a destra (`VEDI TUTTO →` verso `/catalogo`).
   - Griglia a 4 colonne su desktop (`gap: 20px`) con card pulite senza bordi pesanti:
     - Box immagine quadrato/verticale (`aspect-ratio: 4/5`, sfondo `#22201E`) con zoom morbido dell'immagine (`transform: scale(1.04)`) all'hover e badge sottile in alto a sinistra (`TESTA` / `ARMI` / ecc. in `#4E5D3F`).
     - Sotto l'immagine (su sfondo trasparente, stile e-commerce streetwear): nome del **Partner** in piccolo uppercase (`#EED9B3` opacity `0.6`), titolo prodotto bold in `#EED9B3`, e prezzo in `#F08C00` monospace.

3. **Ticker / Marquee Tattico & Sezione Partner/Eventi:**
   - Una barra orizzontale sottile con scorrimento continuo (`marquee` CSS puro) in stile `empi.re`:
     `PEWPEW MF • TACTICAL EQUIPMENT • SOFTAIR ARENA • NOLEGGIO ATTREZZATURA • SUPPORTO TECNICO 24/7 • `
   - Griglia editoriale a 2 colonne per **Eventi & Partner Ufficiali** con foto grandi, titolo oversize e CTA minimale.

---

## 4. Task 3: Uniformare Tutte le Pagine Interne (`catalogo.html`, `prenotazioni.html`, `partner-eventi.html`, `account.html`, `ticket-*.html`, `carrello.html`, `login.html`)
Applica lo stesso design system contemporaneo (`static/css/style.css`) a tutte le pagine già create:
- **Catalogo (`catalogo.html`):** Tab orizzontali minimaliste in alto per filtrare `TUTTI | TESTA | BUSTO | ARMI | GAMBE | ACCESSORI` (stile tab *Trending / Bestsellers / Merch* di `empi.re`, con linea inferiore `#F08C00` sulla tab attiva invece dei vecchi bottoni colorati) + griglia prodotto pulita a 4 colonne.
- **Prenotazione Sessioni (`prenotazioni.html` e `prenotazione-configura.html`):** Card sessioni con layout orizzontale moderno (data grande monospace a sinistra, dettagli campo al centro, prezzo e CTA netta a destra). Nel configuratore noleggio, usa card selezionabili con bordo attivo `#F08C00` e riepilogo sticky minimalista.
- **Account, Carrello & Ticket (`account.html`, `carrello.html`, `ticket-lista.html`, `ticket-dettaglio.html`):** Tabelle senza griglie pesanti (solo divisori orizzontali sottili `1px solid rgba(238,217,179,0.1)`), badge stato compatti e chat ticket stile messenger moderno.

---

## 5. File da Aggiornare (Scope Esatto)
1. `src/main/resources/static/css/style.css` (Nuovo design system completo, import Google Fonts `Space Grotesk`, `Inter`, `JetBrains Mono`, variabili palette, card, bottoni, marquee, navbar).
2. `src/main/resources/static/css/hero-soldier.css` (Aggiornamento stile minimalista dello stage soldato e degli hotspot).
3. `src/main/resources/templates/fragments/navbar.html` (Navbar sticky contemporanea).
4. `src/main/resources/templates/index.html` (Stage soldato rinnovato + vetrina Trending Gear + marquee + sezione Partner/Eventi).
5. Template interni (`catalogo.html`, `articolo-dettaglio.html`, `prenotazioni.html`, `prenotazione-configura.html`, `partner-eventi.html`, `partner-dettaglio.html`, `carrello.html`, `account.html`, `ticket-lista.html`, `ticket-dettaglio.html`, `login.html`, `registrazione.html`) affinché usino le nuove classi CSS moderne senza alterare alcun attributo `th:*`, `name` o URL dei form.

---

## Verifica Finale
1. Avvia l'applicazione e visita tutte le pagine principali (`/`, `/catalogo`, `/prenotazioni`, `/partner`, `/account`, `/ticket`).
2. Verifica che il sito abbia un aspetto editoriale contemporaneo ad alto contrasto ispirato a `empi.re`, con i colori ufficiali **PEWPEW MF** (`#1A1A1A`, `#EED9B3`, `#F08C00`, `#C92A2A`, `#4E5D3F`, `#4A1E1E`).
3. Verifica che la Hero video iniziale e lo zoom interattivo sulle 5 parti del corpo del soldato funzionino esattamente come prima.
4. Chiudi la risposta con il `### REPORT COMMIT` previsto in `00_MASTER_RULES.md`.