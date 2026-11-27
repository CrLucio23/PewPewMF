package it.pewpewmf.platform.controller;

import it.pewpewmf.platform.model.Utente;
import it.pewpewmf.platform.service.BusinessException;
import it.pewpewmf.platform.service.UtenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UtenteService utenti;

    public AuthController(UtenteService utenti) {
        this.utenti = utenti;
    }

    public record RegistrazioneForm(
            @Size(max = 255) String nome,
            @Size(max = 255) String cognome,
            @NotBlank @Size(max = 255) String username,
            @NotBlank @Email @Size(max = 255) String email,
            @NotBlank @Size(min = 8, max = 72) String password) {
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registrazione")
    public String registrazione(Model model) {
        model.addAttribute("registrazione", new RegistrazioneForm("", "", "", "", ""));
        return "registrazione";
    }

    @PostMapping("/registrazione")
    public String registra(@Valid @ModelAttribute("registrazione") RegistrazioneForm form,
                           BindingResult errors) {
        if (form.password() != null && form.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            errors.rejectValue("password", "password.length", "La password deve occupare al massimo 72 byte UTF-8");
        }
        if (errors.hasErrors()) {
            return "registrazione";
        }
        Utente dati = new Utente();
        dati.setNome(form.nome());
        dati.setCognome(form.cognome());
        dati.setUsername(form.username());
        dati.setEmail(form.email());
        dati.setPassword(form.password());
        try {
            utenti.registra(dati);
        } catch (BusinessException ex) {
            errors.reject("registrazione.invalid", ex.getMessage());
            return "registrazione";
        } catch (DataIntegrityViolationException ex) {
            errors.reject("registrazione.duplicate", "Username o email già utilizzati");
            return "registrazione";
        }
        return "redirect:/login?registrazioneSuccess";
    }
}
