package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.Prenotazione;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByUtenteIdOrderByDataCreazioneDesc(Long utenteId);
}
