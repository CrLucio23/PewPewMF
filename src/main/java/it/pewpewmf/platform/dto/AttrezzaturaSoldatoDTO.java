package it.pewpewmf.platform.dto;

import it.pewpewmf.platform.model.enums.ZonaCorpo;
import java.math.BigDecimal;

public record AttrezzaturaSoldatoDTO(Long id, String nome, BigDecimal prezzoAcquisto,
        BigDecimal prezzoNoleggioSessione, ZonaCorpo zonaCorpo, String immagineUrl,
        String partnerNome) {
}
