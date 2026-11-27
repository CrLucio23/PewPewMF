package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.model.enums.PrioritaTicket;
import it.pewpewmf.platform.model.enums.StatoTicket;
import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.TicketSupportoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketSupportoController {
    private final TicketSupportoService tickets;

    public TicketSupportoController(TicketSupportoService tickets) {
        this.tickets = tickets;
    }

    @GetMapping("/ticket")
    public String lista(Model model) {
        boolean staff = tickets.isStaff();
        model.addAttribute("staff", staff);
        model.addAttribute("tickets", staff ? tickets.getTicketsAperti() : tickets.getTicketsUtente());
        model.addAttribute("priorita", PrioritaTicket.values());
        return "ticket-lista";
    }

    @PostMapping("/ticket/nuovo")
    public String nuovo(@RequestParam String oggetto, @RequestParam String categoria,
                         @RequestParam PrioritaTicket priorita, @RequestParam String testo,
                         RedirectAttributes redirect) {
        try {
            Long id = tickets.apriTicket(oggetto, categoria, priorita, testo).getId();
            redirect.addAttribute("id", id);
            return "redirect:/ticket/{id}";
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
            redirect.addFlashAttribute("oggetto", oggetto);
            redirect.addFlashAttribute("categoria", categoria);
            redirect.addFlashAttribute("prioritaSelezionata", priorita);
            redirect.addFlashAttribute("testo", testo);
            return "redirect:/ticket#nuovo";
        }
    }

    @GetMapping("/ticket/{id}")
    public String dettaglio(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", tickets.getTicket(id));
        model.addAttribute("staff", tickets.isStaff());
        model.addAttribute("stati", new StatoTicket[]{StatoTicket.IN_LAVORAZIONE, StatoTicket.RISOLTO, StatoTicket.CHIUSO});
        return "ticket-dettaglio";
    }

    @PostMapping("/ticket/{id}/rispondi")
    public String rispondi(@PathVariable Long id, @RequestParam String testo, RedirectAttributes redirect) {
        try {
            tickets.aggiungiMessaggio(id, testo);
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
            redirect.addFlashAttribute("testo", testo);
        }
        redirect.addAttribute("id", id);
        return "redirect:/ticket/{id}";
    }

    @PostMapping("/ticket/{id}/stato")
    public String stato(@PathVariable Long id, @RequestParam StatoTicket stato, RedirectAttributes redirect) {
        if (!tickets.isStaff()) throw new AccessDeniedException("Operazione riservata allo staff");
        try {
            tickets.cambiaStato(id, stato);
            redirect.addFlashAttribute("conferma", "Stato del ticket aggiornato");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
        }
        redirect.addAttribute("id", id);
        return "redirect:/ticket/{id}";
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Void> nonTrovato() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> inputNonValido() {
        return ResponseEntity.badRequest().build();
    }
}
