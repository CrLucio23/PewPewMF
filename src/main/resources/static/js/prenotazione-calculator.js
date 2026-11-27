(() => {
    'use strict';

    const form = document.getElementById('booking-form');
    if (!form) return;

    const selection = document.getElementById('rental-equipment');
    const rentals = Array.from(form.querySelectorAll('input[name="attrezzatureIds"]'));
    const euro = new Intl.NumberFormat('it-IT', { style: 'currency', currency: 'EUR' });
    const toCents = value => Math.round(Number(value) * 100);
    const base = toCents(form.dataset.basePrice);

    function updateTotal() {
        const renting = form.querySelector('input[name="conNoleggio"]:checked')?.value === 'true';
        selection.hidden = !renting;
        rentals.forEach(input => { input.disabled = !renting; });
        const selected = renting ? rentals.filter(input => input.checked) : [];
        const rentalTotal = selected.reduce((sum, input) => sum + toCents(input.dataset.rentalPrice), 0);
        document.getElementById('base-total').textContent = euro.format(base / 100);
        document.getElementById('rental-total').textContent = euro.format(rentalTotal / 100);
        document.getElementById('booking-total').textContent = euro.format((base + rentalTotal) / 100);
        document.getElementById('equipment-summary').textContent = renting
            ? `${selected.length} articoli selezionati per il noleggio.`
            : 'Ingresso con attrezzatura propria.';
    }

    form.addEventListener('change', updateTotal);
    window.addEventListener('pageshow', updateTotal);
    updateTotal();
})();
