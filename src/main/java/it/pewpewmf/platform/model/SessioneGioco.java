package it.pewpewmf.platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class SessioneGioco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @NotNull
    @Column(nullable = false)
    private String campo;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataOraInizio;

    @NotNull
    @Column(nullable = false)
    private Integer durataOre;

    @NotNull
    @Column(nullable = false)
    private BigDecimal prezzoBaseSessione;

    @NotNull
    @Column(nullable = false)
    private Integer postiTotali;

    @NotNull
    @Column(nullable = false)
    private Integer postiDisponibili;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_associato_id")
    private Evento eventoAssociato;

    public SessioneGioco() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public Integer getDurataOre() {
        return durataOre;
    }

    public void setDurataOre(Integer durataOre) {
        this.durataOre = durataOre;
    }

    public BigDecimal getPrezzoBaseSessione() {
        return prezzoBaseSessione;
    }

    public void setPrezzoBaseSessione(BigDecimal prezzoBaseSessione) {
        this.prezzoBaseSessione = prezzoBaseSessione;
    }

    public Integer getPostiTotali() {
        return postiTotali;
    }

    public void setPostiTotali(Integer postiTotali) {
        this.postiTotali = postiTotali;
    }

    public Integer getPostiDisponibili() {
        return postiDisponibili;
    }

    public void setPostiDisponibili(Integer postiDisponibili) {
        this.postiDisponibili = postiDisponibili;
    }

    public Evento getEventoAssociato() {
        return eventoAssociato;
    }

    public void setEventoAssociato(Evento eventoAssociato) {
        this.eventoAssociato = eventoAssociato;
    }
}
