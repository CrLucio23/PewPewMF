package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.service.AttrezzaturaService;
import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.PrenotazioneService;
import java.util.List;
import org.springframework.http.ResponseEntity;
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
public class PrenotazioneController {
    private final PrenotazioneService prenotazioni;
    private final AttrezzaturaService attrezzature;

    public PrenotazioneController(PrenotazioneService prenotazioni, AttrezzaturaService attrezzature) {
        this.prenotazioni = prenotazioni;
        this.attrezzature = attrezzature;
    }

    @GetMapping("/prenotazioni")
    public String sessioni(Model model) {
        model.addAttribute("sessioni", prenotazioni.getSessioniDisponibili());
        return "prenotazioni";
    }

    @GetMapping("/prenotazioni/sessione/{id}")
    public String configura(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("sessione", prenotazioni.getSessioneDisponibile(id));
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
            return "redirect:/prenotazioni";
        }
        model.addAttribute("attrezzaturePerZona", attrezzature.getAttrezzatureNoleggiabiliPerZona());
        if (!model.containsAttribute("conNoleggio")) {
            model.addAttribute("conNoleggio", false);
        }
        if (!model.containsAttribute("attrezzatureSelezionate")) {
            model.addAttribute("attrezzatureSelezionate", List.of());
        }
        return "prenotazione-configura";
    }

    @PostMapping("/prenotazioni/conferma")
    public String conferma(@RequestParam Long sessioneId, @RequestParam boolean conNoleggio,
                           @RequestParam(required = false) List<Long> attrezzatureIds,
                           RedirectAttributes redirect) {
        if (sessioneId <= 0) {
            redirect.addFlashAttribute("errore", "Sessione non valida");
            return "redirect:/prenotazioni";
        }
        try {
            prenotazioni.creaPrenotazione(sessioneId, conNoleggio, attrezzatureIds);
            return "redirect:/account?prenotazioneSuccess";
        } catch (BusinessException ex) {
            redirect.addFlashAttribute("errore", ex.getMessage());
            redirect.addFlashAttribute("conNoleggio", conNoleggio);
            redirect.addFlashAttribute("attrezzatureSelezionate", attrezzatureIds == null ? List.of() : attrezzatureIds);
            redirect.addAttribute("id", sessioneId);
            return "redirect:/prenotazioni/sessione/{id}";
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> parametroNonValido() {
        return ResponseEntity.badRequest().build();
    }
}
