package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.OrdineService;
import it.pewpewmf.platform.service.PrenotazioneService;
import it.pewpewmf.platform.service.TicketSupportoService;
import it.pewpewmf.platform.service.UtenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {
    private final UtenteService utenti;
    private final OrdineService ordini;
    private final PrenotazioneService prenotazioni;
    private final TicketSupportoService tickets;

    public AccountController(UtenteService utenti, OrdineService ordini,
                             PrenotazioneService prenotazioni, TicketSupportoService tickets) {
        this.utenti = utenti;
        this.ordini = ordini;
        this.prenotazioni = prenotazioni;
        this.tickets = tickets;
    }

    @GetMapping("/account")
    public String account(Model model) {
        model.addAttribute("utente", utenti.getUtenteCorrente());
        model.addAttribute("ordini", ordini.getStoricoUtente());
        model.addAttribute("prenotazioni", prenotazioni.getStoricoUtente());
        model.addAttribute("tickets", tickets.getTicketsUtente());
        return "account";
    }

    @PostMapping("/account/prenotazioni/{id}/annulla")
    public String annulla(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            prenotazioni.annullaPrenotazione(id);
            redirect.addFlashAttribute("conferma", "Prenotazione annullata");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
        }
        return "redirect:/account#prenotazioni";
    }
}
