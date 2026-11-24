package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.Attrezzatura;
import it.pewpewmf.platform.model.enums.ZonaCorpo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttrezzaturaRepository extends JpaRepository<Attrezzatura, Long> {

    List<Attrezzatura> findByZonaCorpo(ZonaCorpo zonaCorpo);

    List<Attrezzatura> findByPartnerId(Long partnerId);

    List<Attrezzatura> findByCategoriaContainingIgnoreCase(String categoria);

    List<Attrezzatura> findByDisponibilitaNoleggioGreaterThan(Integer disponibilitaNoleggio);
}
