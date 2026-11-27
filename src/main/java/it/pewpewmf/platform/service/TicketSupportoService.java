package it.pewpewmf.platform.service;

import it.pewpewmf.platform.model.MessaggioTicket;
import it.pewpewmf.platform.model.TicketSupporto;
import it.pewpewmf.platform.model.Utente;
import it.pewpewmf.platform.model.enums.PrioritaTicket;
import it.pewpewmf.platform.model.enums.RuoloUtente;
import it.pewpewmf.platform.model.enums.StatoTicket;
import it.pewpewmf.platform.repository.TicketSupportoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TicketSupportoService {
    private final TicketSupportoRepository tickets;
    private final UtenteService utenti;
    private final EntityManager entityManager;

    public TicketSupportoService(TicketSupportoRepository tickets, UtenteService utenti,
                                 EntityManager entityManager) {
        this.tickets = tickets;
        this.utenti = utenti;
        this.entityManager = entityManager;
    }

    @Transactional
    public TicketSupporto apriTicket(String oggetto, String categoria, PrioritaTicket priorita,
                                     String testo) {
        if (oggetto == null || oggetto.isBlank() || categoria == null || categoria.isBlank()
                || priorita == null) {
            throw new BusinessException("Compilare oggetto, categoria e priorità");
        }
        verificaTesto(testo);
        Utente autore = utenti.getUtenteCorrente();
        TicketSupporto ticket = new TicketSupporto();
        ticket.setCodiceTicket("TKT-" + UUID.randomUUID());
        ticket.setUtente(autore);
        ticket.setOggetto(oggetto.trim());
        ticket.setCategoria(categoria.trim());
        ticket.setPriorita(priorita);
        ticket.setStato(StatoTicket.APERTO);
        ticket.setDataApertura(LocalDateTime.now());
        ticket.setUltimoAggiornamento(ticket.getDataApertura());
        aggiungiMessaggioInterno(ticket, autore, testo);
        return tickets.save(ticket);
    }

    @Transactional
    public TicketSupporto aggiungiMessaggio(Long id, String testo) {
        verificaTesto(testo);
        Utente autore = utenti.getUtenteCorrente();
        TicketSupporto ticket = carica(id, autore, true);
        if (ticket.getStato() == StatoTicket.CHIUSO) {
            throw new BusinessException("Ticket chiuso");
        }
        aggiungiMessaggioInterno(ticket, autore, testo);
        return tickets.save(ticket);
    }

    @Transactional
    public TicketSupporto cambiaStato(Long id, StatoTicket stato) {
        Utente autore = utenti.getUtenteCorrente();
        if (!staff(autore)) {
            throw new BusinessException("Operazione riservata allo staff");
        }
        if (stato == null || stato == StatoTicket.APERTO) {
            throw new BusinessException("Stato non valido");
        }
        TicketSupporto ticket = carica(id, autore, true);
        ticket.setStato(stato);
        ticket.setUltimoAggiornamento(LocalDateTime.now());
        return ticket;
    }

    public List<TicketSupporto> getTickets() {
        Utente autore = utenti.getUtenteCorrente();
        return staff(autore) ? tickets.findAllByOrderByUltimoAggiornamentoDesc()
                : tickets.findByUtenteIdOrderByUltimoAggiornamentoDesc(autore.getId());
    }

    public boolean isStaff() {
        return staff(utenti.getUtenteCorrente());
    }

    public List<TicketSupporto> getTicketsUtente() {
        return tickets.findByUtenteIdOrderByUltimoAggiornamentoDesc(utenti.getUtenteCorrente().getId());
    }

    public List<TicketSupporto> getTicketsAperti() {
        return getTickets().stream().filter(t -> t.getStato() == StatoTicket.APERTO
                || t.getStato() == StatoTicket.IN_LAVORAZIONE).toList();
    }

    public TicketSupporto getTicket(Long id) {
        TicketSupporto ticket = carica(id, utenti.getUtenteCorrente(), false);
        ticket.getMessaggi().sort(Comparator.comparing(MessaggioTicket::getDataInvio)
                .thenComparing(MessaggioTicket::getId));
        return ticket;
    }

    private TicketSupporto carica(Long id, Utente autore, boolean blocca) {
        TicketSupporto ticket = blocca
                ? entityManager.find(TicketSupporto.class, id, LockModeType.PESSIMISTIC_WRITE)
                : entityManager.find(TicketSupporto.class, id);
        if (blocca && ticket != null) {
            entityManager.refresh(ticket);
        }
        if (ticket == null || !staff(autore) && !autore.getId().equals(ticket.getUtente().getId())) {
            throw new BusinessException("Ticket non trovato");
        }
        return ticket;
    }

    private void aggiungiMessaggioInterno(TicketSupporto ticket, Utente autore, String testo) {
        MessaggioTicket messaggio = new MessaggioTicket();
        messaggio.setTicket(ticket);
        messaggio.setAutore(autore);
        messaggio.setTesto(testo.trim());
        messaggio.setDataInvio(LocalDateTime.now());
        messaggio.setRispostaStaff(staff(autore));
        ticket.getMessaggi().add(messaggio);
        ticket.setUltimoAggiornamento(messaggio.getDataInvio());
    }

    private boolean staff(Utente utente) {
        return utente.getRuolo() == RuoloUtente.SUPPORTO || utente.getRuolo() == RuoloUtente.ADMIN;
    }

    private void verificaTesto(String testo) {
        if (testo == null || testo.isBlank()) {
            throw new BusinessException("Il messaggio è obbligatorio");
        }
    }
}
