package it.pewpewmf.platform.service;

import it.pewpewmf.platform.model.Attrezzatura;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrelloService {
    private final AttrezzaturaService attrezzature;
    private final OrdineService ordini;

    public CarrelloService(AttrezzaturaService attrezzature, OrdineService ordini) {
        this.attrezzature = attrezzature;
        this.ordini = ordini;
    }

    public record Riga(Attrezzatura articolo, int quantita, BigDecimal subtotale) { }
    public record Riepilogo(List<Riga> righe, BigDecimal totale) { }

    @Transactional(readOnly = true)
    public Riepilogo getRiepilogo(HttpSession sessione) {
        Map<Long, Integer> contenuto;
        synchronized (sessione) {
            contenuto = contenuto(sessione);
        }
        List<Riga> righe = new ArrayList<>();
        BigDecimal totale = BigDecimal.ZERO;
        for (var voce : contenuto.entrySet()) {
            Attrezzatura articolo = attrezzature.getAttrezzatura(voce.getKey());
            BigDecimal subtotale = articolo.getPrezzoAcquisto().multiply(BigDecimal.valueOf(voce.getValue()));
            righe.add(new Riga(articolo, voce.getValue(), subtotale));
            totale = totale.add(subtotale);
        }
        return new Riepilogo(List.copyOf(righe), totale);
    }

    @Transactional(readOnly = true)
    public void aggiungi(HttpSession sessione, Long id, int quantita) {
        if (id == null || id <= 0 || quantita <= 0) {
            throw new BusinessException("Articolo o quantità non validi");
        }
        Attrezzatura articolo = attrezzature.getAttrezzatura(id);
        synchronized (sessione) {
            Map<Long, Integer> contenuto = contenuto(sessione);
            long nuovaQuantita = (long) contenuto.getOrDefault(id, 0) + quantita;
            if (nuovaQuantita > articolo.getDisponibilitaVendita()) {
                throw new BusinessException("Disponibilità insufficiente per " + articolo.getNome());
            }
            contenuto.put(id, (int) nuovaQuantita);
            sessione.setAttribute("carrello", contenuto);
        }
    }

    public void rimuovi(HttpSession sessione, Long id) {
        synchronized (sessione) {
            Map<Long, Integer> contenuto = contenuto(sessione);
            contenuto.remove(id);
            sessione.setAttribute("carrello", contenuto);
        }
    }

    public void checkout(HttpSession sessione) {
        synchronized (sessione) {
            ordini.creaOrdine(sessione);
        }
    }

    private Map<Long, Integer> contenuto(HttpSession sessione) {
        Map<Long, Integer> copia = new LinkedHashMap<>();
        Object valore = sessione.getAttribute("carrello");
        if (valore == null) return copia;
        if (!(valore instanceof Map<?, ?> mappa)) throw new BusinessException("Carrello non valido");
        mappa.forEach((chiave, quantita) -> {
            if (!(chiave instanceof Long id) || !(quantita instanceof Integer q) || id <= 0 || q <= 0) {
                throw new BusinessException("Carrello non valido");
            }
            copia.put(id, q);
        });
        return copia;
    }
}
