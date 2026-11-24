package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.TicketSupporto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketSupportoRepository extends JpaRepository<TicketSupporto, Long> {

    List<TicketSupporto> findByUtenteIdOrderByUltimoAggiornamentoDesc(Long utenteId);

    List<TicketSupporto> findAllByOrderByUltimoAggiornamentoDesc();
}
