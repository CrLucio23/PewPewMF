package it.pewpewmf.dao;

import it.pewpewmf.model.Prodotto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Primo DAO di sola lettura, con dati dimostrativi in memoria.
 * Il collegamento JDBC e le operazioni di scrittura appartengono al prossimo lotto.
 */
public class ProdottoDAO {
    private final List<Prodotto> prodotti = List.of(
        new Prodotto(1, "SA-H22-BK", "Replica SA-H22", "Specna Arms", "Repliche",
            "Il punto di partenza per il tuo setup da campo.", new BigDecimal("239.90"), 8),
        new Prodotto(2, "HT-MCDU-OG", "Combat shirt MCDU", "Helikon-Tex", "Abbigliamento",
            "Un capo essenziale per muoverti sul terreno.", new BigDecimal("54.90"), 12),
        new Prodotto(3, "BL-X800-CL", "Maschera X800", "Bollé", "Protezioni",
            "Completa la tua attrezzatura prima della prossima giocata.", new BigDecimal("29.90"), 6),
        new Prodotto(4, "SA-MID-150", "Caricatore mid-cap", "Specna Arms", "Accessori",
            "Un caricatore di scorta per il tuo equipaggiamento.", new BigDecimal("14.90"), 0),
        new Prodotto(5, "BB-025-1KG", "Pallini 0,25 g", "BLS", "Consumabili",
            "Confezione da 1 kg per le tue giornate di gioco.", new BigDecimal("12.50"), 20)
    );

    /** L'elenco e i suoi prodotti sono immutabili e condivisibili fra richieste. */
    public List<Prodotto> findAll() {
        return prodotti;
    }

    public Optional<Prodotto> findById(long id) {
        return prodotti.stream().filter(prodotto -> prodotto.getId() == id).findFirst();
    }
}
