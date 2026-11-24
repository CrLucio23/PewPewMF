package it.pewpewmf.platform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    private String logoUrl;

    private String sitoWeb;

    private String categoriaPartner;

    public Partner() {
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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSitoWeb() {
        return sitoWeb;
    }

    public void setSitoWeb(String sitoWeb) {
        this.sitoWeb = sitoWeb;
    }

    public String getCategoriaPartner() {
        return categoriaPartner;
    }

    public void setCategoriaPartner(String categoriaPartner) {
        this.categoriaPartner = categoriaPartner;
    }
}
