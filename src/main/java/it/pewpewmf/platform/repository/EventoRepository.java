package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.Evento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByPartnersId(Long partnerId);
}
