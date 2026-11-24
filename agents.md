# PEWPEW MF - MASTER RULES & GUARDRAILS PER CODEX

> **REGOLA AUREA:** Leggi sempre questo file prima di eseguire qualsiasi task. Implementa **ESCLUSIVAMENTE** ciò che è richiesto nel file di step corrente. Non generare file extra, non aggiungere dipendenze non richieste e non rifattorizzare file completati negli step precedenti se non esplicitamente indicato.

---

## 1. Stack Tecnologico Vincolante (NON MODIFICARE)
- **Architettura:** Three-Tier + Model-View-Controller (MVC).
- **Linguaggio:** Java 21 (LTS).
- **Framework Back-end:** Spring Boot 3.x + Spring Web MVC (`@Controller`, **NO** `@RestController` salvo endpoint AJAX esplicitamente richiesti per il soldato interattivo).
- **Application Server:** Apache Tomcat (embedded / deploy WAR standard).
- **Template Engine:** Thymeleaf (`src/main/resources/templates/*.html`).
- **Front-end:** HTML5, CSS3 custom + Bootstrap 5 (via CDN), Vanilla JavaScript (ES6+).
- **Persistenza:** Spring Data JPA (Hibernate) + PostgreSQL.
- **Sicurezza:** Spring Security (form login tradizionale con sessione HTTP, **NO** JWT).

---

## 2. Divieti Assoluti per Codex (Anti-Allucinazione)
1. **VIETATO** usare React, Angular, Vue, TypeScript, Node.js, npm, Tailwind o bundler (Webpack/Vite).
2. **VIETATO** sostituire PostgreSQL con H2, MySQL o MongoDB.
3. **VIETATO** inserire logica di business o query nei `@Controller` o nei template Thymeleaf. I Controller devono solo validare input, invocare i `@Service` e popolare il `Model`.
4. **VIETATO** generare pagine o funzionalità di step futuri (es. non creare il carrello o i ticket durante lo step dell'Hero).
5. **VIETATO** inventare colori fuori dalla palette ufficiale definita qui sotto.

---

## 3. Palette Colori Ufficiale & Design System ("PEWPEW MF")
Ogni foglio di stile (`src/main/resources/static/css/style.css`) deve dichiarare e utilizzare **esclusivamente** queste variabili CSS:

```css
:root {
  --color-red-mascot: #C92A2A;     /* Rosso Mascotte: CTA primarie, alert, badge importanti, hover attivi */
  --color-military-green: #4E5D3F; /* Verde Militare: accenti tattici, tag disponibilità, sezioni secondarie */
  --color-gold-orange: #F08C00;    /* Oro/Arancione: prezzi, stelle rating, evidenziatori focus, bordi attivi */
  --color-cream-text: #EED9B3;     /* Crema Scritte: testo principale e titoli su sfondi scuri */
  --color-dark-mountain: #4A1E1E;  /* Scuro Montagne: card, pannelli laterali, moduli e contenitori */
  --color-black-outline: #1A1A1A;  /* Nero Contorni: sfondo principale (body), navbar, bordi netti */
}
```

### Linee Guida Stile Visivo
- **Tema:** Dark Tactical / Softair Mill-Sim.
- **Sfondo generale:** `--color-black-outline` (`#1A1A1A`).
- **Card e Modali:** `--color-dark-mountain` (`#4A1E1E`) con bordo `1px solid var(--color-military-green)` o `var(--color-gold-orange)`.
- **Testi:** `--color-cream-text` (`#EED9B3`) per massima leggibilità.
- **Logo:** Posizionato in `src/main/resources/static/images/logo-pewpew.png` (se non ancora presente fisicamente, usare un fallback SVG inline coerente col brand).

---

## 4. Struttura Package Java Standard
Usa sempre come root package: `it.pewpewmf.platform`
- `it.pewpewmf.platform.config` (SecurityConfig, WebConfig)
- `it.pewpewmf.platform.model` (Entity e Enum)
- `it.pewpewmf.platform.repository` (JpaRepository)
- `it.pewpewmf.platform.service` (Interfacce e Implementazioni Service)
- `it.pewpewmf.platform.controller` (Controller MVC)
- `it.pewpewmf.platform.dto` (Eventuali DTO per form o risposte JSON zoom soldato)