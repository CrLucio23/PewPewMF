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
import java.util.Map;
import java.util.stream.Collectors;
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

    public List<Evento> getEventi() {
        return eventi.findAll().stream()
                .sorted(Comparator.comparing(Evento::getDataInizio)).toList();
    }

    public Map<Long, Long> getConteggiAttrezzature() {
        return attrezzature.findAll().stream().filter(a -> a.getPartner() != null)
                .collect(Collectors.groupingBy(a -> a.getPartner().getId(), Collectors.counting()));
    }

    public Map<Long, Long> getConteggiEventi() {
        return eventi.findAll().stream().flatMap(e -> e.getPartners().stream())
                .collect(Collectors.groupingBy(Partner::getId, Collectors.counting()));
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
