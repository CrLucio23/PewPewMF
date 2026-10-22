<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="it_IT" scope="page" />
<c:url var="catalogoUrl" value="/catalogo" />
<c:url var="cssUrl" value="/assets/css/catalogo.css" />
<c:url var="jsUrl" value="/assets/js/catalogo.js" />
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="PewPewMF: il tuo prossimo setup da softair. Esplora il catalogo dimostrativo.">
    <title>Shop — PewPewMF</title>
    <link rel="stylesheet" href="${cssUrl}">
    <script src="${jsUrl}" defer></script>
</head>
<body>
    <a class="skip-link" href="#contenuto">Vai al catalogo</a>
    <header class="site-header page-width">
        <a class="wordmark" href="${catalogoUrl}" aria-label="PewPewMF, catalogo">PEWPEW<span>MF</span><sup>®</sup></a>
        <nav aria-label="Navigazione principale">
            <a href="${catalogoUrl}" aria-current="page">Shop</a>
            <a href="#categorie">Categorie</a>
            <a href="#progetto">Il progetto</a>
        </nav>
        <span class="header-note mono">ATTREZZATURA. PERSONE. CAMPO.</span>
    </header>

    <!-- Illustrazioni vettoriali provvisorie: nessuna dipendenza da immagini esterne. -->
    <svg class="svg-definitions" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
        <defs>
            <symbol id="icona-Repliche" viewBox="0 0 240 160">
                <path d="M26 70h27l15-10h103v12h39v7h-39v9h-48l-8 37H96l3-37H67l-11 15H42V87H26Z" />
                <path class="detail-line" d="M73 68h84M127 76h32M135 61v26M145 61v26M155 61v26M58 73v16" />
            </symbol>
            <symbol id="icona-Abbigliamento" viewBox="0 0 240 160">
                <path d="m88 29 18-6h28l18 6 29 19 20 71-25 8-23-57v69H87V70l-23 57-25-8 20-71Z" />
                <path class="detail-line" d="m106 24 14 20 14-20M120 44v35M88 53v59M152 53v59" />
            </symbol>
            <symbol id="icona-Protezioni" viewBox="0 0 240 160">
                <path d="M30 64h25v35H30ZM185 64h25v35h-25ZM59 50q61-15 122 0l10 16-8 40q-25 13-50-9l-13-6-13 6q-25 22-50 9l-8-40Z" />
                <path class="detail-line" d="M69 62q51-11 102 0l5 12-6 21q-21 5-35-10l-15-5-15 5q-14 15-35 10l-6-21Z" />
            </symbol>
            <symbol id="icona-Accessori" viewBox="0 0 240 160">
                <path d="M86 28h64l-5 60-19 47H77l13-50Z" />
                <path class="detail-line" d="M98 42h38M99 53h36M101 64h33M102 79l-13 43M114 79l-12 43M126 79l-12 43" />
            </symbol>
            <symbol id="icona-Consumabili" viewBox="0 0 240 160">
                <path d="M76 23h88l-6 22 13 84H69l13-84Z" />
                <path class="detail-line" d="M82 36h76M84 59h72v48H84Z" />
                <g fill="var(--surface)"><circle cx="106" cy="76" r="4"/><circle cx="122" cy="75" r="4"/><circle cx="138" cy="77" r="4"/><circle cx="113" cy="92" r="4"/><circle cx="131" cy="93" r="4"/></g>
            </symbol>
        </defs>
    </svg>

    <main id="contenuto" class="page-width">
        <p class="breadcrumb mono">Shop / Il tuo prossimo setup.</p>
        <section class="catalogue-intro" aria-labelledby="titolo-catalogo">
            <p class="eyebrow mono">SHOP / FIELD ESSENTIALS</p>
            <div class="intro-heading">
                <h1 id="titolo-catalogo">IL TUO PROSSIMO SETUP.</h1>
                <p>Repliche, protezioni e tutto<br> quello che serve davvero.</p>
            </div>
        </section>

        <form id="filtri" role="search" aria-label="Filtra il catalogo">
            <fieldset id="controlli-filtri" class="catalogue-layout" disabled>
                <legend class="visually-hidden">Ricerca, filtri e ordinamento dei prodotti</legend>
                <aside class="filters" aria-label="Filtri prodotti">
                    <fieldset class="filter-group" id="categorie">
                        <legend>Categorie</legend>
                        <c:forEach items="${categorie}" var="categoria">
                            <label class="checkbox-label">
                                <input type="checkbox" name="categoria" value="<c:out value='${categoria}'/>">
                                <span><c:out value="${categoria}" /></span>
                            </label>
                        </c:forEach>
                    </fieldset>
                    <div class="filter-group">
                        <label for="marca">Marca</label>
                        <select id="marca" name="marca">
                            <option value="">Tutte le marche</option>
                            <c:forEach items="${marche}" var="marca">
                                <option value="<c:out value='${marca}'/>"><c:out value="${marca}" /></option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="prezzo-massimo">Prezzo massimo</label>
                        <output id="valore-prezzo" for="prezzo-massimo"><fmt:formatNumber value="${prezzoMassimo}" type="currency" currencySymbol="€" /></output>
                        <input id="prezzo-massimo" name="prezzoMassimo" type="range" min="0" max="${prezzoMassimo}" value="${prezzoMassimo}" step="1">
                    </div>
                    <button type="reset" class="reset-button">Azzera filtri <span aria-hidden="true">↗</span></button>
                    <p class="filter-note">Scegli il tuo setup.<br>Ci vediamo sul campo.</p>
                </aside>

                <div class="catalogue-results">
                    <div class="toolbar">
                        <div class="search-field">
                            <label for="ricerca">Cerca nel catalogo</label>
                            <input id="ricerca" name="q" type="search" placeholder="Nome, marca o codice prodotto…" autocomplete="off">
                        </div>
                        <div>
                            <label for="ordinamento">Ordina per</label>
                            <select id="ordinamento" name="ordinamento">
                                <option value="selezionati">Selezionati</option>
                                <option value="prezzo-crescente">Prezzo crescente</option>
                                <option value="prezzo-decrescente">Prezzo decrescente</option>
                                <option value="nome">Nome: A–Z</option>
                            </select>
                        </div>
                    </div>
                    <p id="conteggio-prodotti" class="result-count mono" role="status" aria-live="polite" aria-atomic="true">${prodotti.size()} prodotti</p>
                    <noscript><p class="notice">Attiva JavaScript per usare ricerca, filtri e ordinamento. Puoi comunque consultare tutti i prodotti.</p></noscript>

                    <div id="griglia-prodotti" class="product-grid">
                        <c:forEach items="${prodotti}" var="prodotto">
                            <article class="product" data-prodotto
                                     data-nome="<c:out value='${prodotto.nome}'/>"
                                     data-codice="<c:out value='${prodotto.codice}'/>"
                                     data-marca="<c:out value='${prodotto.marca}'/>"
                                     data-categoria="<c:out value='${prodotto.categoria}'/>"
                                     data-prezzo="${prodotto.prezzo}">
                                <div class="product-visual">
                                    <span class="product-category mono"><c:out value="${prodotto.categoria}" /></span>
                                    <svg class="product-illustration" aria-hidden="true" viewBox="0 0 240 160">
                                        <use href="#icona-<c:out value='${prodotto.categoria}'/>" />
                                    </svg>
                                    <span class="visual-note mono" aria-hidden="true">FIELD KIT / 0${prodotto.id}</span>
                                </div>
                                <p class="product-code mono"><c:out value="${prodotto.marca}" /> / <c:out value="${prodotto.codice}" /></p>
                                <h2><c:out value="${prodotto.nome}" /></h2>
                                <p class="product-description"><c:out value="${prodotto.descrizione}" /></p>
                                <div class="product-bottom">
                                    <strong class="price"><fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" /></strong>
                                    <c:choose>
                                        <c:when test="${prodotto.disponibile}"><span class="availability">Disponibile</span></c:when>
                                        <c:otherwise><span class="availability sold-out">Esaurito</span></c:otherwise>
                                    </c:choose>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                    <c:if test="${empty prodotti}">
                        <div class="empty-state"><h2>IL CATALOGO SI STA PREPARANDO.</h2><p>Torna presto a scoprire i prodotti.</p></div>
                    </c:if>
                    <div id="nessun-risultato" class="empty-state" hidden>
                        <p class="eyebrow mono">NESSUNA CORRISPONDENZA</p>
                        <h2>CAMBIA PROSPETTIVA.</h2>
                        <p>Prova un'altra ricerca o rimuovi qualche filtro.</p>
                        <button type="reset" class="primary-button">Mostra tutti i prodotti <span aria-hidden="true">↗</span></button>
                    </div>
                </div>
            </fieldset>
        </form>
    </main>

    <footer id="progetto" class="site-footer page-width">
        <p class="footer-title">IL SETUP È SOLO L'INIZIO.</p>
        <div><p>PewPewMF · Progetto universitario</p><p class="muted">Catalogo dimostrativo. Prodotti, prezzi e disponibilità sono di esempio.</p></div>
    </footer>
</body>
</html>
