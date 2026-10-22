package it.pewpewmf.controller;

import it.pewpewmf.dao.ProdottoDAO;
import it.pewpewmf.model.Prodotto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** Carica il catalogo dal DAO e delega la presentazione alla JSP. */
@WebServlet(name = "CatalogoServlet", urlPatterns = {"", "/catalogo"}, loadOnStartup = 1)
public class CatalogoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAO prodottoDAO;

    @Override
    public void init() {
        prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Prodotto> prodotti = prodottoDAO.findAll();
        request.setAttribute("prodotti", prodotti);
        request.setAttribute("categorie", prodotti.stream()
            .map(Prodotto::getCategoria).distinct().toList());
        request.setAttribute("marche", prodotti.stream()
            .map(Prodotto::getMarca).distinct().sorted().toList());
        int prezzoMassimo = prodotti.stream().map(Prodotto::getPrezzo)
            .max(BigDecimal::compareTo).orElse(BigDecimal.ONE)
            .setScale(0, RoundingMode.CEILING).intValueExact();
        request.setAttribute("prezzoMassimo", Math.max(1, prezzoMassimo));

        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("/WEB-INF/views/catalogo.jsp").forward(request, response);
    }
}
