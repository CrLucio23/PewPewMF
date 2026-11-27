package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.PartnerEventoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PartnerEventoController {
    private final PartnerEventoService partnerEventi;

    public PartnerEventoController(PartnerEventoService partnerEventi) {
        this.partnerEventi = partnerEventi;
    }

    @GetMapping("/partner")
    public String partners(Model model) {
        model.addAttribute("partners", partnerEventi.getPartners());
        model.addAttribute("conteggiAttrezzature", partnerEventi.getConteggiAttrezzature());
        model.addAttribute("conteggiEventi", partnerEventi.getConteggiEventi());
        model.addAttribute("eventi", partnerEventi.getEventi());
        return "partner-eventi";
    }

    @GetMapping("/partner/{id}")
    public String partner(@PathVariable Long id, Model model) {
        model.addAttribute("partner", partnerEventi.getPartner(id));
        model.addAttribute("attrezzature", partnerEventi.getAttrezzaturePartner(id));
        model.addAttribute("eventi", partnerEventi.getEventiPartner(id));
        return "partner-dettaglio";
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Void> nonTrovato() {
        return ResponseEntity.notFound().build();
    }
}
