package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.Ordine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {

    List<Ordine> findByUtenteIdOrderByDataOrdineDesc(Long utenteId);
}
