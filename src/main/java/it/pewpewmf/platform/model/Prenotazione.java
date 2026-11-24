package it.pewpewmf.platform.model;

import it.pewpewmf.platform.model.enums.StatoPrenotazione;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String codicePrenotazione;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sessione_gioco_id", nullable = false)
    private SessioneGioco sessioneGioco;

    @NotNull
    @Column(nullable = false)
    private Boolean conNoleggioAttrezzatura;

    @ManyToMany
    @JoinTable(name = "prenotazione_noleggio_attrezzatura",
            joinColumns = @JoinColumn(name = "prenotazione_id"),
            inverseJoinColumns = @JoinColumn(name = "attrezzatura_id"))
    private List<Attrezzatura> attrezzatureNoleggiate = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private BigDecimal totalePagato;

    @Enumerated(EnumType.STRING)
    private StatoPrenotazione stato;

    private LocalDateTime dataCreazione;

    public Prenotazione() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public void setCodicePrenotazione(String codicePrenotazione) {
        this.codicePrenotazione = codicePrenotazione;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public SessioneGioco getSessioneGioco() {
        return sessioneGioco;
    }

    public void setSessioneGioco(SessioneGioco sessioneGioco) {
        this.sessioneGioco = sessioneGioco;
    }

    public Boolean getConNoleggioAttrezzatura() {
        return conNoleggioAttrezzatura;
    }

    public void setConNoleggioAttrezzatura(Boolean conNoleggioAttrezzatura) {
        this.conNoleggioAttrezzatura = conNoleggioAttrezzatura;
    }

    public List<Attrezzatura> getAttrezzatureNoleggiate() {
        return attrezzatureNoleggiate;
    }

    public void setAttrezzatureNoleggiate(List<Attrezzatura> attrezzatureNoleggiate) {
        this.attrezzatureNoleggiate = attrezzatureNoleggiate;
    }

    public BigDecimal getTotalePagato() {
        return totalePagato;
    }

    public void setTotalePagato(BigDecimal totalePagato) {
        this.totalePagato = totalePagato;
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }
}
