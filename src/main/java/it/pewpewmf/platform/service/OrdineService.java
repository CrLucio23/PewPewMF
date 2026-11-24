package it.pewpewmf.platform.service;

import it.pewpewmf.platform.model.Attrezzatura;
import it.pewpewmf.platform.model.DettaglioOrdine;
import it.pewpewmf.platform.model.Ordine;
import it.pewpewmf.platform.repository.OrdineRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Transactional(readOnly = true)
public class OrdineService {
    private final OrdineRepository ordini;
    private final UtenteService utenti;
    private final EntityManager entityManager;

    public OrdineService(OrdineRepository ordini, UtenteService utenti, EntityManager entityManager) {
        this.ordini = ordini;
        this.utenti = utenti;
        this.entityManager = entityManager;
    }

    // L'attributo di sessione "carrello" contiene una Map<Long, Integer>: id articolo -> quantità.
    @Transactional
    public Ordine creaOrdine(HttpSession sessione) {
        Ordine ordine = new Ordine();
        ordine.setUtente(utenti.getUtenteCorrente());
        Object contenuto = sessione.getAttribute("carrello");
        if (!(contenuto instanceof Map<?, ?> carrello) || carrello.isEmpty()) {
            throw new BusinessException("Carrello vuoto");
        }
        Map<Long, Integer> righe = new TreeMap<>();
        carrello.forEach((chiave, valore) -> {
            if (!(chiave instanceof Long id) || !(valore instanceof Integer quantita)
                    || id <= 0 || quantita <= 0) {
                throw new BusinessException("Carrello non valido");
            }
            righe.put(id, quantita);
        });
        BigDecimal totale = BigDecimal.ZERO;
        for (var riga : righe.entrySet()) {
            Attrezzatura a = entityManager.find(Attrezzatura.class, riga.getKey(),
                    LockModeType.PESSIMISTIC_WRITE);
            if (a == null) {
                throw new BusinessException("Attrezzatura non trovata");
            }
            entityManager.refresh(a);
            if (a.getDisponibilitaVendita() < riga.getValue()) {
                throw new BusinessException("Disponibilità insufficiente per " + a.getNome());
            }
            DettaglioOrdine dettaglio = new DettaglioOrdine();
            dettaglio.setOrdine(ordine);
            dettaglio.setAttrezzatura(a);
            dettaglio.setQuantita(riga.getValue());
            dettaglio.setPrezzoUnitario(a.getPrezzoAcquisto());
            ordine.getDettagli().add(dettaglio);
            totale = totale.add(a.getPrezzoAcquisto().multiply(BigDecimal.valueOf(riga.getValue())));
            a.setDisponibilitaVendita(a.getDisponibilitaVendita() - riga.getValue());
        }
        ordine.setCodiceOrdine("ORD-" + UUID.randomUUID());
        ordine.setDataOrdine(LocalDateTime.now());
        ordine.setTotale(totale);
        ordine.setStatoOrdine("CONFERMATO");
        Ordine salvato = ordini.saveAndFlush(ordine);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (contenuto.equals(sessione.getAttribute("carrello"))) {
                    sessione.removeAttribute("carrello");
                }
            }
        });
        return salvato;
    }

    public List<Ordine> getStoricoUtente() {
        return ordini.findByUtenteIdOrderByDataOrdineDesc(utenti.getUtenteCorrente().getId());
    }
}
