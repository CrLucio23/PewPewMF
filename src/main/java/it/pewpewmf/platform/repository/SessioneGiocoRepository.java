package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.SessioneGioco;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessioneGiocoRepository extends JpaRepository<SessioneGioco, Long> {

    List<SessioneGioco> findByDataOraInizioAfterOrderByDataOraInizioAsc(LocalDateTime dataOraInizio);
}
