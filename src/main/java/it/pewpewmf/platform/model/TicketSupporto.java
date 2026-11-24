package it.pewpewmf.platform.model;

import it.pewpewmf.platform.model.enums.PrioritaTicket;
import it.pewpewmf.platform.model.enums.StatoTicket;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TicketSupporto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codiceTicket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    private String oggetto;

    private String categoria;

    @Enumerated(EnumType.STRING)
    private PrioritaTicket priorita;

    @Enumerated(EnumType.STRING)
    private StatoTicket stato;

    private LocalDateTime dataApertura;

    private LocalDateTime ultimoAggiornamento;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<MessaggioTicket> messaggi = new ArrayList<>();

    public TicketSupporto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodiceTicket() {
        return codiceTicket;
    }

    public void setCodiceTicket(String codiceTicket) {
        this.codiceTicket = codiceTicket;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public PrioritaTicket getPriorita() {
        return priorita;
    }

    public void setPriorita(PrioritaTicket priorita) {
        this.priorita = priorita;
    }

    public StatoTicket getStato() {
        return stato;
    }

    public void setStato(StatoTicket stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(LocalDateTime dataApertura) {
        this.dataApertura = dataApertura;
    }

    public LocalDateTime getUltimoAggiornamento() {
        return ultimoAggiornamento;
    }

    public void setUltimoAggiornamento(LocalDateTime ultimoAggiornamento) {
        this.ultimoAggiornamento = ultimoAggiornamento;
    }

    public List<MessaggioTicket> getMessaggi() {
        return messaggi;
    }

    public void setMessaggi(List<MessaggioTicket> messaggi) {
        this.messaggi = messaggi;
    }
}
