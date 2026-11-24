package it.pewpewmf.platform.service;

import it.pewpewmf.platform.model.Attrezzatura;
import it.pewpewmf.platform.model.Evento;
import it.pewpewmf.platform.model.Partner;
import it.pewpewmf.platform.repository.AttrezzaturaRepository;
import it.pewpewmf.platform.repository.EventoRepository;
import it.pewpewmf.platform.repository.PartnerRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PartnerEventoService {
    private final PartnerRepository partners;
    private final EventoRepository eventi;
    private final AttrezzaturaRepository attrezzature;

    public PartnerEventoService(PartnerRepository partners, EventoRepository eventi,
                                AttrezzaturaRepository attrezzature) {
        this.partners = partners;
        this.eventi = eventi;
        this.attrezzature = attrezzature;
    }

    public List<Partner> getPartners() {
        return partners.findAll();
    }

    public Partner getPartner(Long id) {
        return partners.findById(id).orElseThrow(() -> new BusinessException("Partner non trovato"));
    }

    public List<Attrezzatura> getAttrezzaturePartner(Long id) {
        getPartner(id);
        return attrezzature.findByPartnerId(id);
    }

    public List<Evento> getEventiPartner(Long id) {
        getPartner(id);
        return eventi.findByPartnersId(id);
    }

    public List<Evento> getEventiFuturi() {
        LocalDateTime ora = LocalDateTime.now();
        return eventi.findAll().stream().filter(e -> !e.getDataInizio().isBefore(ora))
                .sorted(Comparator.comparing(Evento::getDataInizio)).toList();
    }

    public List<Evento> getEventiPassati() {
        LocalDateTime ora = LocalDateTime.now();
        return eventi.findAll().stream().filter(e -> e.getDataFine().isBefore(ora))
                .sorted(Comparator.comparing(Evento::getDataInizio).reversed()).toList();
    }

    public Evento getEvento(Long id) {
        Evento evento = eventi.findById(id)
                .orElseThrow(() -> new BusinessException("Evento non trovato"));
        evento.getPartners().size();
        return evento;
    }
}
