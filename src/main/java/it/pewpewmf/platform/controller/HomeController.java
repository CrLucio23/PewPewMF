package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.dto.AttrezzaturaSoldatoDTO;
import it.pewpewmf.platform.model.enums.ZonaCorpo;
import it.pewpewmf.platform.service.AttrezzaturaService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    private final AttrezzaturaService attrezzature;

    public HomeController(AttrezzaturaService attrezzature) {
        this.attrezzature = attrezzature;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/api/soldato/zona/{zona}")
    @ResponseBody
    public List<AttrezzaturaSoldatoDTO> attrezzaturePerZona(@PathVariable ZonaCorpo zona) {
        return attrezzature.getAttrezzatureByZonaCorpo(zona);
    }
}
