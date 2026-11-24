package it.pewpewmf.platform.model;

import it.pewpewmf.platform.model.enums.ZonaCorpo;
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
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Attrezzatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String nome;

    @NotNull
    @Column(nullable = false, unique = true)
    private String codiceSku;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @NotNull
    @Column(nullable = false)
    private BigDecimal prezzoAcquisto;

    @NotNull
    @Column(nullable = false)
    private BigDecimal prezzoNoleggioSessione;

    @NotNull
    @Column(nullable = false)
    private Integer disponibilitaVendita;

    @NotNull
    @Column(nullable = false)
    private Integer disponibilitaNoleggio;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private ZonaCorpo zonaCorpo;

    private String categoria;

    private String immagineUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public Attrezzatura() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodiceSku() {
        return codiceSku;
    }

    public void setCodiceSku(String codiceSku) {
        this.codiceSku = codiceSku;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public void setPrezzoAcquisto(BigDecimal prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }

    public BigDecimal getPrezzoNoleggioSessione() {
        return prezzoNoleggioSessione;
    }

    public void setPrezzoNoleggioSessione(BigDecimal prezzoNoleggioSessione) {
        this.prezzoNoleggioSessione = prezzoNoleggioSessione;
    }

    public Integer getDisponibilitaVendita() {
        return disponibilitaVendita;
    }

    public void setDisponibilitaVendita(Integer disponibilitaVendita) {
        this.disponibilitaVendita = disponibilitaVendita;
    }

    public Integer getDisponibilitaNoleggio() {
        return disponibilitaNoleggio;
    }

    public void setDisponibilitaNoleggio(Integer disponibilitaNoleggio) {
        this.disponibilitaNoleggio = disponibilitaNoleggio;
    }

    public ZonaCorpo getZonaCorpo() {
        return zonaCorpo;
    }

    public void setZonaCorpo(ZonaCorpo zonaCorpo) {
        this.zonaCorpo = zonaCorpo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImmagineUrl() {
        return immagineUrl;
    }

    public void setImmagineUrl(String immagineUrl) {
        this.immagineUrl = immagineUrl;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }
}
