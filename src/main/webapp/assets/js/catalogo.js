"use strict";

(() => {
    const form = document.querySelector("#filtri");
    if (!form) return;

    const controlli = document.querySelector("#controlli-filtri");
    const ricerca = document.querySelector("#ricerca");
    const marca = document.querySelector("#marca");
    const prezzoMassimo = document.querySelector("#prezzo-massimo");
    const valorePrezzo = document.querySelector("#valore-prezzo");
    const ordinamento = document.querySelector("#ordinamento");
    const categorie = [...form.querySelectorAll('input[name="categoria"]')];
    const griglia = document.querySelector("#griglia-prodotti");
    const conteggio = document.querySelector("#conteggio-prodotti");
    const nessunRisultato = document.querySelector("#nessun-risultato");
    const euro = new Intl.NumberFormat("it-IT", { style: "currency", currency: "EUR" });
    const collator = new Intl.Collator("it", { sensitivity: "base", numeric: true });

    function normalizza(testo) {
        return testo.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLocaleLowerCase("it").trim();
    }

    // I dati arrivano dal server; il browser filtra soltanto questo piccolo catalogo.
    const prodotti = [...griglia.querySelectorAll("[data-prodotto]")].map((elemento, indice) => ({
        elemento,
        indice,
        nome: elemento.dataset.nome,
        marca: elemento.dataset.marca,
        categoria: elemento.dataset.categoria,
        prezzoCentesimi: Math.round(Number(elemento.dataset.prezzo) * 100),
        testo: normalizza(`${elemento.dataset.nome} ${elemento.dataset.marca} ${elemento.dataset.codice}`)
    }));

    function aggiornaCatalogo() {
        const termini = normalizza(ricerca.value).split(/\s+/).filter(Boolean);
        const selezionate = new Set(categorie.filter(categoria => categoria.checked).map(categoria => categoria.value));
        const tettoCentesimi = Math.round(Number(prezzoMassimo.value) * 100);
        let visibili = 0;
        valorePrezzo.textContent = euro.format(tettoCentesimi / 100);

        for (const prodotto of prodotti) {
            const visibile = termini.every(termine => prodotto.testo.includes(termine))
                && (!marca.value || prodotto.marca === marca.value)
                && (selezionate.size === 0 || selezionate.has(prodotto.categoria))
                && prodotto.prezzoCentesimi <= tettoCentesimi;
            prodotto.elemento.hidden = !visibile;
            if (visibile) visibili += 1;
        }

        const ordinati = [...prodotti].sort((a, b) => {
            switch (ordinamento.value) {
                case "prezzo-crescente": return a.prezzoCentesimi - b.prezzoCentesimi || a.indice - b.indice;
                case "prezzo-decrescente": return b.prezzoCentesimi - a.prezzoCentesimi || a.indice - b.indice;
                case "nome": return collator.compare(a.nome, b.nome) || a.indice - b.indice;
                default: return a.indice - b.indice;
            }
        });
        griglia.append(...ordinati.map(prodotto => prodotto.elemento));
        conteggio.textContent = `${visibili} ${visibili === 1 ? "prodotto" : "prodotti"}`;
        nessunRisultato.hidden = visibili > 0 || prodotti.length === 0;
    }

    form.addEventListener("submit", evento => evento.preventDefault());
    ricerca.addEventListener("input", aggiornaCatalogo);
    prezzoMassimo.addEventListener("input", aggiornaCatalogo);
    marca.addEventListener("change", aggiornaCatalogo);
    ordinamento.addEventListener("change", aggiornaCatalogo);
    categorie.forEach(categoria => categoria.addEventListener("change", aggiornaCatalogo));
    form.addEventListener("reset", () => {
        // Il browser ripristina i controlli dopo l'evento reset.
        setTimeout(() => {
            aggiornaCatalogo();
            ricerca.focus();
        }, 0);
    });
    window.addEventListener("pageshow", aggiornaCatalogo);
    controlli.disabled = false;
    aggiornaCatalogo();
})();
