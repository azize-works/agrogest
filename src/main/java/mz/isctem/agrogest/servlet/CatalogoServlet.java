package mz.isctem.agrogest.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.CategoriaProduto;
import mz.isctem.agrogest.model.Fornecedor;
import mz.isctem.agrogest.service.FornecedorService;
import mz.isctem.agrogest.service.ProdutoService;

/**
 * Catalogo de produtos para o cliente (/catalogo).
 * Mostra todos os produtos (com filtro opcional por categoria) e o nome do respectivo fornecedor.
 */
@WebServlet("/catalogo")
public class CatalogoServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/cliente/catalogo.jsp";

    /** Mostra o catalogo de produtos, filtrado opcionalmente pela categoria. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarCatalogo(req, resp);
    }

    /** Prepara os produtos, as categorias e o mapa id -> nome do fornecedor, e abre a JSP do catalogo. */
    static void mostrarCatalogo(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String categoria = req.getParameter("categoria");
        List<Fornecedor> fornecedores = FornecedorService.listar("nome");
        Map<Integer, String> nomesFornecedores = new HashMap<>();
        for (Fornecedor f : fornecedores) {
            nomesFornecedores.put(f.getId(), f.getNome());
        }

        req.setAttribute("produtos", ProdutoService.listar("nome", categoria));
        req.setAttribute("categorias", CategoriaProduto.values());
        req.setAttribute("filtroCategoria", categoria);
        req.setAttribute("nomesFornecedores", nomesFornecedores);
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}
