package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.model.Attrezzatura;
import it.pewpewmf.platform.model.enums.ZonaCorpo;
import it.pewpewmf.platform.service.AttrezzaturaService;
import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.PartnerEventoService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Controller
public class CatalogoController {
    private final AttrezzaturaService attrezzature;
    private final PartnerEventoService partnerEventi;

    public CatalogoController(AttrezzaturaService attrezzature, PartnerEventoService partnerEventi) {
        this.attrezzature = attrezzature;
        this.partnerEventi = partnerEventi;
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) ZonaCorpo zona,
                           @RequestParam(required = false) Long partnerId,
                           @RequestParam(required = false) String q, Model model) {
        if (partnerId != null) {
            partnerEventi.getPartner(partnerId);
        }
        model.addAttribute("attrezzature", attrezzature.getCatalogo(zona, partnerId, q, null, null));
        model.addAttribute("partners", partnerEventi.getPartners());
        model.addAttribute("zone", ZonaCorpo.values());
        model.addAttribute("zona", zona);
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("q", q);
        return "catalogo";
    }

    @GetMapping("/catalogo/articolo/{id}")
    public String articolo(@PathVariable Long id, Model model) {
        Attrezzatura articolo = attrezzature.getAttrezzatura(id);
        model.addAttribute("articolo", articolo);
        model.addAttribute("eventi", articolo.getPartner() == null ? List.of()
                : partnerEventi.getEventiPartner(articolo.getPartner().getId()));
        return "articolo-dettaglio";
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Void> nonTrovato() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Void> filtroNonValido() {
        return ResponseEntity.badRequest().build();
    }
}
