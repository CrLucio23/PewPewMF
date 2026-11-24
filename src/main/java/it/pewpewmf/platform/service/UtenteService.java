package it.pewpewmf.platform.service;

import it.pewpewmf.platform.model.Utente;
import it.pewpewmf.platform.model.enums.RuoloUtente;
import it.pewpewmf.platform.repository.UtenteRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UtenteService {
    private final UtenteRepository utenti;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenti, PasswordEncoder passwordEncoder) {
        this.utenti = utenti;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Utente registra(Utente dati) {
        if (dati == null || dati.getUsername() == null || dati.getUsername().isBlank()
                || dati.getEmail() == null || dati.getEmail().isBlank()
                || dati.getPassword() == null || dati.getPassword().isBlank()) {
            throw new BusinessException("Username, email e password sono obbligatori");
        }
        String username = dati.getUsername().trim();
        String email = dati.getEmail().trim();
        if (utenti.existsByUsername(username) || utenti.existsByEmail(email)) {
            throw new BusinessException("Username o email già utilizzati");
        }
        Utente utente = new Utente();
        utente.setNome(dati.getNome());
        utente.setCognome(dati.getCognome());
        utente.setUsername(username);
        utente.setEmail(email);
        utente.setPassword(passwordEncoder.encode(dati.getPassword()));
        utente.setRuolo(RuoloUtente.CLIENTE);
        utente.setDataRegistrazione(LocalDateTime.now());
        return utenti.saveAndFlush(utente);
    }

    public Utente getUtenteCorrente() {
        return getUtenteCorrente(SecurityContextHolder.getContext().getAuthentication());
    }

    public Utente getUtenteCorrente(Principal principal) {
        if (principal == null || principal instanceof AnonymousAuthenticationToken
                || (principal instanceof Authentication auth && !auth.isAuthenticated())) {
            throw new BusinessException("Autenticazione richiesta");
        }
        return utenti.findByUsername(principal.getName())
                .orElseThrow(() -> new BusinessException("Utente non trovato"));
    }
}
