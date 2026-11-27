package it.pewpewmf.platform.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/carrello", "/carrello/aggiungi", "/carrello/rimuovi").permitAll()
                        .requestMatchers("/", "/catalogo/**", "/partner/**", "/eventi/**",
                                "/api/attrezzature/**", "/api/soldato/zona/**", "/login", "/registrazione",
                                "/css/**", "/js/**", "/images/**", "/img/**").permitAll()
                        .requestMatchers("/prenotazioni/**", "/carrello/**", "/account/**",
                                "/ticket/**").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/account", false).permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/?logout"))
                .build();
    }
}
