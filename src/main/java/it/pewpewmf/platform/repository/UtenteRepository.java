package it.pewpewmf.platform.repository;

import it.pewpewmf.platform.model.Utente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
