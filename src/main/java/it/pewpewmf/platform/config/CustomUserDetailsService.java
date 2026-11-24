package it.pewpewmf.platform.config;

import it.pewpewmf.platform.repository.UtenteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private final UtenteRepository utenti;

    public CustomUserDetailsService(UtenteRepository utenti) {
        this.utenti = utenti;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        var utente = utenti.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
        return User.withUsername(utente.getUsername())
                .password(utente.getPassword())
                .authorities("ROLE_" + utente.getRuolo().name())
                .build();
    }
}
