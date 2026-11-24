package it.pewpewmf.platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class MessaggioTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TicketSupporto ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autore_id")
    private Utente autore;

    @Column(columnDefinition = "TEXT")
    private String testo;

    private LocalDateTime dataInvio;

    private Boolean rispostaStaff;

    public MessaggioTicket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TicketSupporto getTicket() {
        return ticket;
    }

    public void setTicket(TicketSupporto ticket) {
        this.ticket = ticket;
    }

    public Utente getAutore() {
        return autore;
    }

    public void setAutore(Utente autore) {
        this.autore = autore;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDateTime getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(LocalDateTime dataInvio) {
        this.dataInvio = dataInvio;
    }

    public Boolean getRispostaStaff() {
        return rispostaStaff;
    }

    public void setRispostaStaff(Boolean rispostaStaff) {
        this.rispostaStaff = rispostaStaff;
    }
}
