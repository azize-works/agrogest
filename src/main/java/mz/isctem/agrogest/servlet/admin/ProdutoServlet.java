package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
import mz.isctem.agrogest.util.Numeros;

/**
 * Gestao de produtos (/admin/produtos).
 * GET mostra a lista (ordem e categoria) e o formulario; POST cria, repoe stock ou remove.
 */
@WebServlet("/admin/produtos")
public class ProdutoServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/produtos.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarPagina(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accao = req.getParameter("accao");
        try {
            String aviso;
            if ("criar".equals(accao)) {
                ProdutoService.criar(req.getParameter("nome"), req.getParameter("categoria"),
                        req.getParameter("fornecedorId"), req.getParameter("preco"),
                        req.getParameter("quantidade"), req.getParameter("unidade"),
                        req.getParameter("descricao"), req.getParameter("fotoUrl"),
                        req.getParameter("validade"));
                aviso = "Produto criado.";
            } else if ("repor".equals(accao)) {
                int id = Numeros.lerInt(req.getParameter("id"), "Pedido inválido.");
                double quantidade = Numeros.lerDouble(req.getParameter("quantidade"),
                        "Indique a quantidade a repor.");
                ProdutoService.reporStock(id, quantidade);
                aviso = "Stock reposto.";
            } else if ("remover".equals(accao)) {
                ProdutoService.remover(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Produto removido.";
            } else {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            // Post/Redirect/Get: volta a pagina com o aviso verde
            resp.sendRedirect(req.getContextPath() + "/admin/produtos?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // Erro de validacao: mostra a pagina com a mensagem (os campos mantem-se)
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Prepara a lista filtrada/ordenada, as categorias, os fornecedores e o mapa id -> nome; abre a JSP. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Num POST com erro, "categoria" e o campo do formulario e nao o filtro: so filtra no GET
        String filtro = "GET".equals(req.getMethod()) ? req.getParameter("categoria") : null;

        List<Fornecedor> fornecedores = FornecedorService.listar("nome");
        req.setAttribute("produtos", ProdutoService.listar(req.getParameter("ordem"), filtro));
        req.setAttribute("filtro", filtro);
        req.setAttribute("categorias", CategoriaProduto.values());
        req.setAttribute("fornecedores", fornecedores);
        req.setAttribute("nomesFornecedores", nomesPorId(fornecedores));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    /** Mapa id do fornecedor -> nome, para a tabela mostrar o nome em vez do id. */
    static Map<Integer, String> nomesPorId(List<Fornecedor> fornecedores) {
        Map<Integer, String> nomes = new HashMap<>();
        for (Fornecedor f : fornecedores) {
            nomes.put(f.getId(), f.getNome());
        }
        return nomes;
    }
}
