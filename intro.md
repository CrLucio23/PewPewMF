STEP EXTRA: Hero Video a Schermo Intero Stile Editoriale (intro.md)

Prerequisito: Leggi agents.md.
Obiettivo Commit: Rinnovare l'introduzione della pagina principale (index.html) ispirandosi all'estetica minimale ed editoriale di 363sudbury.com, mostrando un video a tutto schermo (intro.mp4) e un pulsante centrale raffinato per iniziare a esplorare l'equipaggiamento e il catalogo, mantenendo la transizione fluida verso lo stage del soldato interattivo già esistente.
NON generare: Nuovi Controller, nuovi Service, modifiche al database o dipendenze esterne.

Task 1: Verifica Posizione Video & Path Statico Spring Boot

L'utente ha posizionato il file video in src/img/intro.mp4 (oppure src/main/resources/static/img/intro.mp4).

Importante per Spring Boot / Tomcat:

Se il file si trova fuori da src/main/resources/static/ (ad esempio in src/img/intro.mp4), configura un ResourceHandler in WebConfig.java (o copialo in src/main/resources/static/img/intro.mp4) affinché sia servito correttamente dall'URL /img/intro.mp4.

Nel tag <video> HTML usa th:src="@{/img/intro.mp4}".

Task 2: Restyling Hero Intro (templates/index.html e static/css/hero-soldier.css)

Sostituisci il vecchio contenitore SVG nella Hero iniziale (#hero-intro-overlay) con un layout video full-bleed (100vw x 100vh) minimale, elegante e cinematografico ispirato a 363sudbury.com:

Video Background Full-Screen:

Tag <video id="intro-video" autoplay muted loop playsinline preload="auto"> che copre l'intera viewport (position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover;).

Overlay scuro leggero sopra il video (background: linear-gradient(180deg, rgba(26,26,26,0.35) 0%, rgba(26,26,26,0.65) 100%)) per garantire il perfetto contrasto dei testi e dei controlli.

Tipografia & Layout Stile Editoriale (363sudbury.com):

Composizione pulita, ariosa e centrata con ampio respiro visivo (niente box pesanti o bordi spessi).

Micro-intestazione superiore (letter-spacing ampio 0.25em, uppercase, font piccolo 0.75rem, colore #EED9B3 con opacità 0.85):
PEWPEW MF — TACTICAL GEAR & FIELD OPERATIONS

Titolo principale minimale (tipografia pulita, colore #EED9B3, peso leggero/medio, grande impatto):
Equipaggiamento tattico, pronto per il campo.

Pulsante Centrale per Esplorare:

Stile trasparente/minimalista con bordo sottile 1px solid rgba(238, 217, 179, 0.65), sfondo rgba(26, 26, 26, 0.30) con backdrop-filter: blur(6px), padding generoso (14px 34px), testo uppercase con letter-spacing: 0.18em in #EED9B3:
ESPLORA L'ARMERIA

Effetto hover raffinato: transizione fluida (0.35s ease) verso sfondo #C92A2A (Rosso Mascotte), bordo #C92A2A e leggero sollevamento.

Dettagli agli angoli (stile architettonico/editoriale):

In basso a sinistra: indicatore discreto SCROLL O CLICCA PER ENTRARE (#EED9B3, opacity 0.6).

In basso a destra: pulsante mute/unmute minimalista per l'audio del video (AUDIO OFF / ON) e link rapido secondario Vai al Catalogo → (/catalogo) in #F08C00.

Task 3: Transizione Fluida verso lo Stage Interattivo (static/js/soldier-interactive.js)

Al click sul pulsante "ESPLORA L'ARMERIA" (oppure scorrendo verso il basso):

L'overlay video (#hero-intro-overlay) esegue una dissolvenza morbida verso l'alto/opacità (opacity: 0; transform: translateY(-20px); pointer-events: none; transition: all 0.8s cubic-bezier(0.22, 1, 0.36, 1)), mettendo in pausa il video per risparmiare risorse.

Rivela subito sotto lo stage principale già esistente con il Soldato Kittato Interattivo (soldato-kittato.jpg) e i 5 hotspot cliccabili (TESTA, BUSTO, ARMI, GAMBE, ACCESSORI).

Mantieni il tasto "Rivedi Intro" nell'angolo in alto a destra dello stage interattivo per riaprire l'overlay video e far ripartire la riproduzione.
