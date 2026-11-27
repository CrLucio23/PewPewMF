package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.CarrelloService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CarrelloController {
    private final CarrelloService carrello;

    public CarrelloController(CarrelloService carrello) {
        this.carrello = carrello;
    }

    @GetMapping("/carrello")
    public String carrello(HttpSession sessione, Model model) {
        model.addAttribute("riepilogo", carrello.getRiepilogo(sessione));
        return "carrello";
    }

    @PostMapping("/carrello/aggiungi")
    public String aggiungi(@RequestParam Long attrezzaturaId, @RequestParam(defaultValue = "1") int quantita,
                           HttpSession sessione, RedirectAttributes redirect) {
        try {
            carrello.aggiungi(sessione, attrezzaturaId, quantita);
            redirect.addFlashAttribute("conferma", "Articolo aggiunto al carrello");
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
        }
        return "redirect:/carrello";
    }

    @PostMapping("/carrello/rimuovi")
    public String rimuovi(@RequestParam Long attrezzaturaId, HttpSession sessione) {
        carrello.rimuovi(sessione, attrezzaturaId);
        return "redirect:/carrello";
    }

    @PostMapping("/carrello/checkout")
    public String checkout(HttpSession sessione, RedirectAttributes redirect) {
        try {
            carrello.checkout(sessione);
            return "redirect:/account?ordineSuccess";
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
            return "redirect:/carrello";
        }
    }

    @ExceptionHandler({BusinessException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Void> inputNonValido() {
        return ResponseEntity.badRequest().build();
    }
}
