package it.pewpewmf.platform.service;

import it.pewpewmf.platform.model.*;
import it.pewpewmf.platform.model.enums.StatoPrenotazione;
import it.pewpewmf.platform.repository.PrenotazioneRepository;
import it.pewpewmf.platform.repository.SessioneGiocoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioni;
    private final SessioneGiocoRepository sessioni;
    private final UtenteService utenti;
    private final EntityManager entityManager;

    public PrenotazioneService(PrenotazioneRepository prenotazioni, SessioneGiocoRepository sessioni,
                               UtenteService utenti, EntityManager entityManager) {
        this.prenotazioni = prenotazioni;
        this.sessioni = sessioni;
        this.utenti = utenti;
        this.entityManager = entityManager;
    }

    public List<SessioneGioco> getSessioniDisponibili() {
        return sessioni.findByDataOraInizioAfterOrderByDataOraInizioAsc(LocalDateTime.now())
                .stream().filter(s -> s.getPostiDisponibili() > 0).toList();
    }

    public SessioneGioco getSessioneDisponibile(Long id) {
        SessioneGioco sessione = sessioni.findById(id)
                .orElseThrow(() -> new BusinessException("Sessione non trovata"));
        verificaSessione(sessione);
        return sessione;
    }

    @Transactional
    public Prenotazione creaPrenotazione(Long sessioneId, boolean conNoleggio,
                                         Collection<Long> attrezzatureIds) {
        return confermaPrenotazione(sessioneId, conNoleggio, attrezzatureIds);
    }

    public BigDecimal calcolaPreventivo(Long sessioneId, boolean conNoleggio,
                                        Collection<Long> attrezzaturaIds) {
        SessioneGioco sessione = sessioni.findById(sessioneId)
                .orElseThrow(() -> new BusinessException("Sessione non trovata"));
        verificaSessione(sessione);
        return totale(sessione, seleziona(conNoleggio, attrezzaturaIds, false));
    }

    @Transactional
    public Prenotazione confermaPrenotazione(Long sessioneId, boolean conNoleggio,
                                             Collection<Long> attrezzaturaIds) {
        Utente utente = utenti.getUtenteCorrente();
        SessioneGioco sessione = entityManager.find(SessioneGioco.class, sessioneId,
                LockModeType.PESSIMISTIC_WRITE);
        if (sessione == null) {
            throw new BusinessException("Sessione non trovata");
        }
        entityManager.refresh(sessione);
        verificaSessione(sessione);
        List<Attrezzatura> selezionate = seleziona(conNoleggio, attrezzaturaIds, true);
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setCodicePrenotazione("PRE-" + UUID.randomUUID());
        prenotazione.setUtente(utente);
        prenotazione.setSessioneGioco(sessione);
        prenotazione.setConNoleggioAttrezzatura(conNoleggio);
        prenotazione.getAttrezzatureNoleggiate().addAll(selezionate);
        prenotazione.setTotalePagato(totale(sessione, selezionate));
        prenotazione.setStato(StatoPrenotazione.CONFERMATA);
        prenotazione.setDataCreazione(LocalDateTime.now());
        sessione.setPostiDisponibili(sessione.getPostiDisponibili() - 1);
        selezionate.forEach(a -> a.setDisponibilitaNoleggio(a.getDisponibilitaNoleggio() - 1));
        return prenotazioni.save(prenotazione);
    }

    public List<Prenotazione> getStoricoUtente() {
        return prenotazioni.findByUtenteIdOrderByDataCreazioneDesc(utenti.getUtenteCorrente().getId());
    }

    @Transactional
    public void annullaPrenotazione(Long id) {
        Utente utente = utenti.getUtenteCorrente();
        Prenotazione prenotazione = entityManager.find(Prenotazione.class, id,
                LockModeType.PESSIMISTIC_WRITE);
        if (prenotazione != null) {
            entityManager.refresh(prenotazione);
        }
        if (prenotazione == null || !utente.getId().equals(prenotazione.getUtente().getId())) {
            throw new BusinessException("Prenotazione non trovata");
        }
        if (prenotazione.getStato() != StatoPrenotazione.CONFERMATA) {
            throw new BusinessException("Prenotazione non annullabile");
        }
        SessioneGioco sessione = entityManager.find(SessioneGioco.class,
                prenotazione.getSessioneGioco().getId(), LockModeType.PESSIMISTIC_WRITE);
        entityManager.refresh(sessione);
        if (!sessione.getDataOraInizio().isAfter(LocalDateTime.now())) {
            throw new BusinessException("La sessione è già iniziata");
        }
        List<Long> ids = prenotazione.getAttrezzatureNoleggiate().stream()
                .map(Attrezzatura::getId).sorted().toList();
        for (Long attrezzaturaId : ids) {
            Attrezzatura a = entityManager.find(Attrezzatura.class, attrezzaturaId,
                    LockModeType.PESSIMISTIC_WRITE);
            entityManager.refresh(a);
            a.setDisponibilitaNoleggio(a.getDisponibilitaNoleggio() + 1);
        }
        sessione.setPostiDisponibili(sessione.getPostiDisponibili() + 1);
        prenotazione.setStato(StatoPrenotazione.ANNULLATA);
    }

    private void verificaSessione(SessioneGioco sessione) {
        if (!sessione.getDataOraInizio().isAfter(LocalDateTime.now())
                || sessione.getPostiDisponibili() <= 0) {
            throw new BusinessException("Sessione non disponibile");
        }
    }

    private List<Attrezzatura> seleziona(boolean conNoleggio, Collection<Long> ids, boolean blocca) {
        if (!conNoleggio || ids == null || ids.isEmpty()) {
            return List.of();
        }
        if (ids.stream().anyMatch(id -> id == null)) {
            throw new BusinessException("Attrezzatura non valida");
        }
        return ids.stream().distinct().sorted().map(id -> {
            Attrezzatura a = blocca
                    ? entityManager.find(Attrezzatura.class, id, LockModeType.PESSIMISTIC_WRITE)
                    : entityManager.find(Attrezzatura.class, id);
            if (a == null) {
                throw new BusinessException("Attrezzatura non trovata");
            }
            if (blocca) {
                entityManager.refresh(a);
            }
            if (a.getDisponibilitaNoleggio() <= 0) {
                throw new BusinessException("Noleggio esaurito per " + a.getNome());
            }
            return a;
        }).toList();
    }

    private BigDecimal totale(SessioneGioco sessione, List<Attrezzatura> selezionate) {
        return selezionate.stream().map(Attrezzatura::getPrezzoNoleggioSessione)
                .reduce(sessione.getPrezzoBaseSessione(), BigDecimal::add);
    }
}
