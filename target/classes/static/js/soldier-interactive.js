(() => {
    'use strict';

    const intro = document.getElementById('hero-intro-overlay');
    const main = document.getElementById('main-content');
    const enter = document.getElementById('enter-armory');
    const replay = document.getElementById('replay-intro');
    const video = document.getElementById('intro-video');
    const audio = document.getElementById('intro-audio');
    const navigation = document.querySelector('.site-nav');
    const camera = document.getElementById('soldier-camera');
    const stage = document.getElementById('interactive-soldier-stage');
    const panel = document.getElementById('equipment-panel');
    if (!intro || !main || !camera || !panel) return;

    const reset = document.getElementById('reset-zoom');
    const close = document.getElementById('close-panel');
    const title = document.getElementById('panel-title');
    const description = document.getElementById('panel-description');
    const viewLabel = document.getElementById('view-label');
    const list = document.getElementById('equipment-list');
    const status = document.getElementById('equipment-status');
    const emptyState = document.getElementById('panel-empty-state');
    const catalog = document.getElementById('filtered-catalog');
    const retry = document.getElementById('retry-equipment');
    const hotspots = Array.from(document.querySelectorAll('[data-zona]'));
    const reducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)');
    const storageKey = 'pewpew-mf.intro-seen';
    const euro = new Intl.NumberFormat('it-IT', { style: 'currency', currency: 'EUR' });
    const zones = {
        TESTA: { label: 'Testa', scale: 2.3, x: 50, y: 15, description: 'Caschi, occhiali e maschere. La protezione viene prima di tutto.' },
        BUSTO: { label: 'Busto', scale: 2.1, x: 50, y: 38, description: 'Plate carrier, chest rig e protezioni. Organizza il tuo setup.' },
        ARMI: { label: 'Armi', scale: 2.2, x: 62, y: 48, description: 'Repliche ASG e componenti per la tua prossima sessione.' },
        GAMBE: { label: 'Gambe', scale: 2.0, x: 50, y: 78, description: 'Pantaloni combat, ginocchiere e anfibi. Muoviti sul tuo terreno.' },
        ACCESSORI: { label: 'Accessori', scale: 2.2, x: 35, y: 52, description: 'Radio, ottiche e accessori. Sono i dettagli a completare il setup.' }
    };
    let activeZone = null;
    let request = null;
    let requestVersion = 0;
    let introTimer = null;
    let previousHotspot = null;
    let introSeen = false;
    let touchStartY = null;

    function updateAudio() {
        if (!video || !audio) return;
        audio.textContent = video.muted ? 'AUDIO OFF' : 'AUDIO ON';
        audio.setAttribute('aria-pressed', String(!video.muted));
        audio.setAttribute('aria-label', video.muted ? 'Attiva audio del video' : 'Disattiva audio del video');
    }

    function playIntro() {
        if (!video || intro.hidden || intro.classList.contains('is-leaving') || document.hidden) return;
        const playback = video.play();
        if (playback) playback.catch(() => {
            video.muted = true;
            updateAudio();
        });
    }

    if (video) {
        video.muted = true;
        video.pause();
        video.addEventListener('volumechange', updateAudio);
        video.addEventListener('error', () => {
            if (audio) audio.disabled = true;
        });
    }
    audio?.addEventListener('click', () => {
        if (!video) return;
        video.muted = !video.muted;
        updateAudio();
        playIntro();
    });

    try {
        introSeen = sessionStorage.getItem(storageKey) === 'true';
    } catch (_) {
        // L'intro resta utilizzabile anche con lo storage disabilitato.
    }

    function showIntro() {
        window.clearTimeout(introTimer);
        intro.hidden = false;
        intro.classList.remove('is-leaving');
        document.body.classList.add('intro-active');
        main.inert = true;
        if (navigation) navigation.inert = true;
        if (video && video.readyState > 0) video.currentTime = 0;
        if (!reducedMotion.matches) playIntro();
        updateAudio();
        enter.focus({ preventScroll: true });
    }

    function finishIntro() {
        if (!intro.classList.contains('is-leaving')) return;
        intro.hidden = true;
        main.inert = false;
        if (navigation) navigation.inert = false;
        document.body.classList.remove('intro-active');
        replay.hidden = false;
        main.focus({ preventScroll: true });
    }

    function leaveIntro() {
        if (intro.hidden || intro.classList.contains('is-leaving')) return;
        try {
            sessionStorage.setItem(storageKey, 'true');
        } catch (_) {
            // La navigazione continua senza memorizzare la preferenza.
        }
        intro.classList.add('is-leaving');
        video?.pause();
        if (reducedMotion.matches) {
            finishIntro();
        } else {
            introTimer = window.setTimeout(finishIntro, 850);
        }
    }

    enter.addEventListener('click', leaveIntro);
    replay.addEventListener('click', showIntro);
    intro.addEventListener('wheel', event => {
        if (event.deltaY > 18 && !event.ctrlKey) leaveIntro();
    }, { passive: true });
    intro.addEventListener('touchstart', event => {
        touchStartY = event.touches.length === 1 ? event.touches[0].clientY : null;
    }, { passive: true });
    intro.addEventListener('touchend', event => {
        if (touchStartY !== null && event.changedTouches.length
                && touchStartY - event.changedTouches[0].clientY > 40) leaveIntro();
        touchStartY = null;
    }, { passive: true });
    intro.addEventListener('keydown', event => {
        if (event.key === 'ArrowDown' || event.key === 'PageDown') {
            event.preventDefault();
            leaveIntro();
        }
        if (event.key !== 'Tab') return;
        const controls = Array.from(intro.querySelectorAll('button:not(:disabled), a[href]'));
        const first = controls[0];
        const last = controls[controls.length - 1];
        if (event.shiftKey && document.activeElement === first) {
            event.preventDefault();
            last?.focus();
        } else if (!event.shiftKey && document.activeElement === last) {
            event.preventDefault();
            first?.focus();
        }
    });
    intro.addEventListener('transitionend', event => {
        if (event.target === intro && event.propertyName === 'opacity') finishIntro();
    });
    replay.hidden = false;
    if (!introSeen) showIntro();

    const navToggle = document.getElementById('nav-toggle');
    const navLinks = document.getElementById('nav-links');
    navLinks.classList.add('is-collapsed');
    function closeNav() {
        navToggle.setAttribute('aria-expanded', 'false');
        navLinks.classList.add('is-collapsed');
    }
    navToggle.addEventListener('click', () => {
        const expanded = navToggle.getAttribute('aria-expanded') === 'true';
        navToggle.setAttribute('aria-expanded', String(!expanded));
        navLinks.classList.toggle('is-collapsed', expanded);
    });
    navLinks.addEventListener('click', event => {
        if (event.target.closest('a')) closeNav();
    });

    const logo = document.querySelector('[data-brand-logo]');
    const fallbackLogo = document.querySelector('[data-brand-fallback]');
    if (logo && fallbackLogo) {
        logo.addEventListener('load', () => {
            logo.hidden = false;
            fallbackLogo.hidden = true;
        });
        logo.addEventListener('error', () => {
            logo.hidden = true;
            fallbackLogo.hidden = false;
        });
        logo.src = logo.dataset.logoSrc;
    }

    function node(tag, className, text) {
        const element = document.createElement(tag);
        if (className) element.className = className;
        if (text !== undefined) element.textContent = text;
        return element;
    }

    function imageFallback(zone) {
        const fallback = node('div', 'equipment-image image-fallback', zones[zone].label);
        fallback.setAttribute('role', 'img');
        fallback.setAttribute('aria-label', 'Immagine articolo non disponibile');
        return fallback;
    }

    function safeImageUrl(value) {
        if (typeof value !== 'string' || !value.trim()) return null;
        try {
            const url = new URL(value, window.location.origin);
            return ['http:', 'https:'].includes(url.protocol) ? url.href : null;
        } catch (_) {
            return null;
        }
    }

    function productCard(item, zone) {
        const card = node('article', 'equipment-card');
        const source = safeImageUrl(item.immagineUrl);
        if (source) {
            const image = node('img', 'equipment-image');
            image.alt = item.nome;
            image.loading = 'lazy';
            image.width = 94;
            image.height = 108;
            image.addEventListener('error', () => image.replaceWith(imageFallback(zone)), { once: true });
            image.src = source;
            card.append(image);
        } else {
            card.append(imageFallback(zone));
        }
        const content = node('div', 'equipment-content');
        content.append(node('h3', '', item.nome));
        content.append(node('p', 'equipment-partner', item.partnerNome || 'Partner non indicato'));
        const prices = node('dl', 'equipment-prices');
        [
            ['Acquisto', item.prezzoAcquisto],
            ['Noleggio / sessione', item.prezzoNoleggioSessione]
        ].forEach(([label, value]) => {
            const price = node('div');
            price.append(node('dt', '', label), node('dd', '', euro.format(Number(value))));
            prices.append(price);
        });
        const detail = node('a', 'detail-link', 'Vedi Dettaglio ↗');
        detail.href = panel.dataset.detailBase + encodeURIComponent(item.id);
        content.append(prices, detail);
        card.append(content);
        return card;
    }

    async function loadEquipment(zone) {
        if (request) request.abort();
        const version = ++requestVersion;
        const controller = new AbortController();
        request = controller;
        const timeout = window.setTimeout(() => controller.abort(), 12000);
        list.replaceChildren();
        list.setAttribute('aria-busy', 'true');
        retry.hidden = true;
        status.textContent = 'Caricamento equipaggiamento…';
        try {
            const response = await fetch(panel.dataset.apiBase + encodeURIComponent(zone), {
                signal: controller.signal,
                headers: { Accept: 'application/json' },
                credentials: 'same-origin'
            });
            if (!response.ok) throw new Error('Risposta non valida');
            const items = await response.json();
            if (!Array.isArray(items) || items.some(item => !item || !Number.isSafeInteger(item.id)
                    || typeof item.nome !== 'string' || item.zonaCorpo !== zone
                    || item.prezzoAcquisto == null || !Number.isFinite(Number(item.prezzoAcquisto))
                    || item.prezzoNoleggioSessione == null || !Number.isFinite(Number(item.prezzoNoleggioSessione)))) {
                throw new Error('Dati non validi');
            }
            if (version !== requestVersion) return;
            const fragment = document.createDocumentFragment();
            items.forEach(item => fragment.append(productCard(item, zone)));
            list.replaceChildren(fragment);
            status.textContent = items.length
                ? items.length + (items.length === 1 ? ' articolo disponibile' : ' articoli disponibili')
                : 'Nessun articolo per questa zona. Esplora il catalogo completo.';
        } catch (_) {
            if (version !== requestVersion) return;
            status.textContent = 'Equipaggiamento non disponibile al momento. Riprova tra poco.';
            retry.hidden = false;
        } finally {
            window.clearTimeout(timeout);
            if (version === requestVersion) list.setAttribute('aria-busy', 'false');
        }
    }

    function selectZone(zone, button) {
        const settings = zones[zone];
        if (!settings) return;
        activeZone = zone;
        previousHotspot = button;
        stage.classList.add('is-zoomed');
        camera.style.transformOrigin = settings.x + '% ' + settings.y + '%';
        camera.style.transform = 'translateX(-50%) translate(' + (50 - settings.x) + '%, '
            + (50 - settings.y) + '%) scale(' + settings.scale + ')';
        hotspots.forEach(hotspot => hotspot.setAttribute('aria-pressed', String(hotspot.dataset.zona === zone)));
        viewLabel.textContent = settings.label + ' / zoom ' + settings.scale.toFixed(1) + '×';
        title.textContent = settings.label;
        description.textContent = settings.description;
        emptyState.hidden = true;
        reset.hidden = false;
        close.hidden = false;
        catalog.hidden = false;
        const url = new URL(panel.dataset.catalogBase, window.location.origin);
        url.searchParams.set('zona', zone);
        catalog.href = url.pathname + url.search;
        catalog.textContent = 'Filtra tutto il Catalogo per ' + settings.label + ' ↗';
        loadEquipment(zone);
    }

    function resetZoom() {
        if (!activeZone) return;
        ++requestVersion;
        if (request) request.abort();
        activeZone = null;
        stage.classList.remove('is-zoomed');
        camera.style.removeProperty('transform');
        camera.style.removeProperty('transform-origin');
        hotspots.forEach(hotspot => hotspot.setAttribute('aria-pressed', 'false'));
        viewLabel.textContent = 'Vista intera';
        title.replaceChildren(document.createTextNode('Ogni dettaglio'), document.createElement('br'),
            document.createTextNode('fa la differenza.'));
        description.textContent = "Scegli una zona dell'operatore per scoprire repliche, protezioni e accessori. Acquista il tuo setup o noleggialo per la prossima sessione.";
        list.replaceChildren();
        list.setAttribute('aria-busy', 'false');
        status.textContent = '';
        emptyState.hidden = false;
        reset.hidden = true;
        close.hidden = true;
        catalog.hidden = true;
        retry.hidden = true;
        previousHotspot?.focus({ preventScroll: true });
    }

    hotspots.forEach(button => button.addEventListener('click', () => selectZone(button.dataset.zona, button)));
    reset.addEventListener('click', resetZoom);
    close.addEventListener('click', resetZoom);
    retry.addEventListener('click', () => { if (activeZone) loadEquipment(activeZone); });
    document.addEventListener('keydown', event => {
        if (event.key !== 'Escape') return;
        if (!intro.hidden) leaveIntro();
        else if (navToggle.getAttribute('aria-expanded') === 'true') {
            closeNav();
            navToggle.focus();
        } else resetZoom();
    });
    document.addEventListener('visibilitychange', () => {
        document.body.classList.toggle('is-background', document.hidden);
        if (document.hidden) video?.pause();
        else if (!reducedMotion.matches) playIntro();
    });
    window.addEventListener('pagehide', () => video?.pause());
    window.addEventListener('pageshow', () => { if (!reducedMotion.matches) playIntro(); });
    reducedMotion.addEventListener('change', () => {
        if (reducedMotion.matches) video?.pause();
        else playIntro();
    });
})();
