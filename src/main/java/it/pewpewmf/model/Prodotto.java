package it.pewpewmf.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Prodotto immutabile: i prezzi sono espressi in euro con due decimali. */
public final class Prodotto {
    private final long id;
    private final String codice;
    private final String nome;
    private final String marca;
    private final String categoria;
    private final String descrizione;
    private final BigDecimal prezzo;
    private final int quantitaDisponibile;

    public Prodotto(long id, String codice, String nome, String marca,
                    String categoria, String descrizione, BigDecimal prezzo,
                    int quantitaDisponibile) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'id deve essere positivo.");
        }
        Objects.requireNonNull(prezzo, "Il prezzo è obbligatorio.");
        if (prezzo.signum() < 0 || quantitaDisponibile < 0) {
            throw new IllegalArgumentException("Prezzo e quantità non possono essere negativi.");
        }
        this.id = id;
        this.codice = testoObbligatorio(codice, "codice");
        this.nome = testoObbligatorio(nome, "nome");
        this.marca = testoObbligatorio(marca, "marca");
        this.categoria = testoObbligatorio(categoria, "categoria");
        this.descrizione = testoObbligatorio(descrizione, "descrizione");
        this.prezzo = prezzo.setScale(2, RoundingMode.UNNECESSARY);
        this.quantitaDisponibile = quantitaDisponibile;
    }

    private static String testoObbligatorio(String valore, String campo) {
        if (valore == null || valore.isBlank()) {
            throw new IllegalArgumentException("Il campo " + campo + " è obbligatorio.");
        }
        return valore.strip();
    }

    public long getId() { return id; }
    public String getCodice() { return codice; }
    public String getNome() { return nome; }
    public String getMarca() { return marca; }
    public String getCategoria() { return categoria; }
    public String getDescrizione() { return descrizione; }
    public BigDecimal getPrezzo() { return prezzo; }
    public int getQuantitaDisponibile() { return quantitaDisponibile; }
    public boolean isDisponibile() { return quantitaDisponibile > 0; }
}
