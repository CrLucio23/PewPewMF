package it.pewpewmf.platform.service;

import it.pewpewmf.platform.dto.AttrezzaturaSoldatoDTO;
import it.pewpewmf.platform.model.Attrezzatura;
import it.pewpewmf.platform.model.enums.ZonaCorpo;
import it.pewpewmf.platform.repository.AttrezzaturaRepository;
import java.util.List;
import java.util.Locale;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AttrezzaturaService {
    private final AttrezzaturaRepository attrezzature;

    public AttrezzaturaService(AttrezzaturaRepository attrezzature) {
        this.attrezzature = attrezzature;
    }

    public List<Attrezzatura> getCatalogo() {
        return attrezzature.findAll();
    }

    public Map<ZonaCorpo, List<Attrezzatura>> getAttrezzatureNoleggiabiliPerZona() {
        List<Attrezzatura> disponibili = attrezzature.findByDisponibilitaNoleggioGreaterThan(0);
        Map<ZonaCorpo, List<Attrezzatura>> perZona = new EnumMap<>(ZonaCorpo.class);
        for (ZonaCorpo zona : ZonaCorpo.values()) {
            perZona.put(zona, disponibili.stream().filter(a -> a.getZonaCorpo() == zona).toList());
        }
        return perZona;
    }

    public List<Attrezzatura> getCatalogo(ZonaCorpo zona, Long partnerId, String keyword,
                                        Boolean disponibileVendita, Boolean disponibileNoleggio) {
        String ricerca = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        return attrezzature.findAll().stream()
                .filter(a -> zona == null || a.getZonaCorpo() == zona)
                .filter(a -> partnerId == null || a.getPartner() != null
                        && partnerId.equals(a.getPartner().getId()))
                .filter(a -> ricerca.isEmpty() || contiene(a.getNome(), ricerca)
                        || contiene(a.getCategoria(), ricerca) || contiene(a.getDescrizione(), ricerca)
                        || contiene(a.getCodiceSku(), ricerca))
                .filter(a -> !Boolean.TRUE.equals(disponibileVendita) || a.getDisponibilitaVendita() > 0)
                .filter(a -> !Boolean.TRUE.equals(disponibileNoleggio) || a.getDisponibilitaNoleggio() > 0)
                .toList();
    }

    public Attrezzatura getAttrezzatura(Long id) {
        return attrezzature.findById(id)
                .orElseThrow(() -> new BusinessException("Attrezzatura non trovata"));
    }

    public List<AttrezzaturaSoldatoDTO> getAttrezzatureByZonaCorpo(ZonaCorpo zona) {
        if (zona == null) {
            throw new BusinessException("Zona del corpo obbligatoria");
        }
        return attrezzature.findByZonaCorpo(zona).stream()
                .map(a -> new AttrezzaturaSoldatoDTO(a.getId(), a.getNome(), a.getPrezzoAcquisto(),
                        a.getPrezzoNoleggioSessione(), a.getZonaCorpo(), a.getImmagineUrl(),
                        a.getPartner() == null ? null : a.getPartner().getNome()))
                .toList();
    }

    private boolean contiene(String valore, String ricerca) {
        return valore != null && valore.toLowerCase(Locale.ROOT).contains(ricerca);
    }
}
