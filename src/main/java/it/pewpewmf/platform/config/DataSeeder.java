package it.pewpewmf.platform.config;

import it.pewpewmf.platform.model.*;
import it.pewpewmf.platform.model.enums.RuoloUtente;
import it.pewpewmf.platform.model.enums.ZonaCorpo;
import it.pewpewmf.platform.repository.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UtenteRepository utenti;
    private final PartnerRepository partners;
    private final AttrezzaturaRepository attrezzature;
    private final EventoRepository eventi;
    private final SessioneGiocoRepository sessioni;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UtenteRepository utenti, PartnerRepository partners,
                      AttrezzaturaRepository attrezzature, EventoRepository eventi,
                      SessioneGiocoRepository sessioni, PasswordEncoder passwordEncoder) {
        this.utenti = utenti;
        this.partners = partners;
        this.attrezzature = attrezzature;
        this.eventi = eventi;
        this.sessioni = sessioni;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (utenti.count() != 0) {
            return;
        }
        LocalDateTime ora = LocalDateTime.now();
        creaUtente("operator", "Marco", "Rossi", RuoloUtente.CLIENTE, ora);
        creaUtente("support", "Giulia", "Bianchi", RuoloUtente.SUPPORTO, ora);

        Partner krytac = creaPartner("Krytac Tactical Italy", "Produttore Repliche",
                "Repliche elettriche e accessori per il softair tattico.");
        Partner tmc = creaPartner("TMC Gear Europe", "Equipaggiamento Tattico",
                "Abbigliamento e protezioni per sessioni mil-sim.");
        Partner bunker = creaPartner("Bunker 42 Arena", "Campo Tattico",
                "Arena CQB con scenari urbani e percorsi boschivi.");

        creaAttrezzatura("Casco FAST Ballistic Replica", "TESTA-001", ZonaCorpo.TESTA,
                "Caschi & Maschere", "Replica da softair con attacchi laterali e imbottitura regolabile.",
                "69.90", "8.00", 12, 16, tmc);
        creaAttrezzatura("Maschera Mesh + Occhiali Balistici", "TESTA-002", ZonaCorpo.TESTA,
                "Caschi & Maschere", "Set di protezione viso e occhi per il gioco sul campo.",
                "39.90", "5.00", 20, 24, tmc);
        creaAttrezzatura("Plate Carrier JPC 2.0 Coyote", "BUSTO-001", ZonaCorpo.BUSTO,
                "Plate Carrier", "Portapiastre replica con sistema MOLLE e tasche modulari.",
                "109.90", "12.00", 10, 14, tmc);
        creaAttrezzatura("Chest Rig Tattico Modulare", "BUSTO-002", ZonaCorpo.BUSTO,
                "Chest Rig", "Pettorina leggera regolabile con tasche portacaricatori.",
                "79.90", "9.00", 14, 18, tmc);
        creaAttrezzatura("Fucile M4A1 AEG Full Metal", "ARMI-001", ZonaCorpo.ARMI,
                "ASG Fucili", "Replica elettrica M4 con corpo in metallo e calcio regolabile.",
                "289.90", "25.00", 8, 12, krytac);
        creaAttrezzatura("Pistola Glock 17 Gas Blowback", "ARMI-002", ZonaCorpo.ARMI,
                "ASG Pistole", "Replica secondaria a gas con carrello scarrellante.",
                "139.90", "15.00", 10, 10, krytac);
        creaAttrezzatura("Pantaloni Combat G3 con Ginocchiere", "GAMBE-001", ZonaCorpo.GAMBE,
                "Anfibi & Pantaloni", "Pantaloni rinforzati con ginocchiere integrate e tasche cargo.",
                "89.90", "10.00", 18, 20, tmc);
        creaAttrezzatura("Anfibi Tattici Impermeabili", "GAMBE-002", ZonaCorpo.GAMBE,
                "Anfibi & Pantaloni", "Calzature con supporto alla caviglia e suola per terreni misti.",
                "119.90", "12.00", 16, 16, tmc);
        creaAttrezzatura("Radio Ricetrasmittente PMR", "ACCESSORI-001", ZonaCorpo.ACCESSORI,
                "Comunicazioni", "Radio PMR per coordinare la squadra durante le sessioni.",
                "49.90", "6.00", 22, 24, bunker);
        creaAttrezzatura("Ottica Red Dot Olografica", "ACCESSORI-002", ZonaCorpo.ACCESSORI,
                "Ottiche", "Ottica replica a punto rosso per acquisizione rapida in CQB.",
                "59.90", "7.00", 15, 18, krytac);

        Evento urban = creaEvento("Operazione Bunker 42",
                "Giornata CQB con obiettivi a squadre e briefing introduttivo.",
                "Bunker 42 Arena - Milano", ora.plusDays(14).withHour(9).withMinute(0),
                List.of(krytac, bunker));
        Evento bosco = creaEvento("Mil-Sim Bosco Rosso",
                "Scenario boschivo con controllo settori e missioni di ricognizione.",
                "Campo Bosco Rosso - Varese", ora.plusDays(28).withHour(8).withMinute(0),
                List.of(tmc, bunker));

        creaSessione("Urban CQB Notturno", "Bunker 42 - Area urbana",
                "Sessione serale a squadre con obiettivi dinamici.",
                urban.getDataInizio().withHour(20), 3, "30.00", 20, urban);
        creaSessione("Dominazione Boschiva", "Bosco Rosso - Settore Nord",
                "Conquista e difesa di postazioni nel bosco.",
                bosco.getDataInizio(), 5, "45.00", 32, bosco);
        creaSessione("Addestramento Reclute", "Bunker 42 - Campo scuola",
                "Briefing, prove pratiche e partita guidata per nuovi giocatori.",
                ora.plusDays(7).withHour(10).withMinute(0), 2, "20.00", 12, null);
    }

    private void creaUtente(String username, String nome, String cognome, RuoloUtente ruolo,
                             LocalDateTime ora) {
        Utente utente = new Utente();
        utente.setUsername(username);
        utente.setNome(nome);
        utente.setCognome(cognome);
        utente.setEmail(username + "@pewpewmf.example");
        utente.setPassword(passwordEncoder.encode("password"));
        utente.setRuolo(ruolo);
        utente.setDataRegistrazione(ora);
        utenti.save(utente);
    }

    private Partner creaPartner(String nome, String categoria, String descrizione) {
        Partner partner = new Partner();
        partner.setNome(nome);
        partner.setCategoriaPartner(categoria);
        partner.setDescrizione(descrizione);
        return partners.save(partner);
    }

    private void creaAttrezzatura(String nome, String sku, ZonaCorpo zona, String categoria,
                                  String descrizione, String acquisto, String noleggio,
                                  int vendita, int disponibiliNoleggio, Partner partner) {
        Attrezzatura a = new Attrezzatura();
        a.setNome(nome);
        a.setCodiceSku(sku);
        a.setZonaCorpo(zona);
        a.setCategoria(categoria);
        a.setDescrizione(descrizione);
        a.setPrezzoAcquisto(new BigDecimal(acquisto));
        a.setPrezzoNoleggioSessione(new BigDecimal(noleggio));
        a.setDisponibilitaVendita(vendita);
        a.setDisponibilitaNoleggio(disponibiliNoleggio);
        a.setPartner(partner);
        attrezzature.save(a);
    }

    private Evento creaEvento(String titolo, String descrizione, String luogo,
                               LocalDateTime inizio, List<Partner> collegati) {
        Evento evento = new Evento();
        evento.setTitolo(titolo);
        evento.setDescrizione(descrizione);
        evento.setLuogo(luogo);
        evento.setDataInizio(inizio);
        evento.setDataFine(inizio.plusHours(14));
        evento.getPartners().addAll(collegati);
        return eventi.save(evento);
    }

    private void creaSessione(String titolo, String campo, String descrizione, LocalDateTime inizio,
                              int durata, String prezzo, int posti, Evento evento) {
        SessioneGioco sessione = new SessioneGioco();
        sessione.setTitolo(titolo);
        sessione.setCampo(campo);
        sessione.setDescrizione(descrizione);
        sessione.setDataOraInizio(inizio);
        sessione.setDurataOre(durata);
        sessione.setPrezzoBaseSessione(new BigDecimal(prezzo));
        sessione.setPostiTotali(posti);
        sessione.setPostiDisponibili(posti);
        sessione.setEventoAssociato(evento);
        sessioni.save(sessione);
    }
}
