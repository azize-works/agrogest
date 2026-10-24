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

import mz.isctem.agrogest.model.Carrinho;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.model.Venda;
import mz.isctem.agrogest.model.VendaItem;
import mz.isctem.agrogest.service.CarrinhoService;
import mz.isctem.agrogest.service.ProdutoService;
import mz.isctem.agrogest.service.VendaService;

/**
 * Confirmacao de compra (/compra), exclusiva para o perfil CLIENTE.
 * GET mostra o resumo da compra; POST finaliza a venda, esvazia o carrinho e abre a factura.
 */
@WebServlet("/compra")
public class CompraServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/cliente/compra.jsp";

    /** Mostra o resumo da compra (itens, subtotal, IVA, total e dados do cliente). */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!CarrinhoServlet.exigirCliente(req, resp)) {
            return;
        }
        Carrinho carrinho = CarrinhoServlet.obterOuCriarCarrinho(req.getSession());
        List<VendaItem> linhas = CarrinhoService.linhas(carrinho);
        if (linhas.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/carrinho");
            return;
        }

        Map<Integer, String> unidades = new HashMap<>();
        for (Produto p : ProdutoService.listar("nome", null)) {
            unidades.put(p.getId(), p.getUnidade());
        }

        req.setAttribute("linhas", linhas);
        req.setAttribute("unidades", unidades);
        req.setAttribute("subtotal", CarrinhoService.subtotal(carrinho));
        req.setAttribute("iva", CarrinhoService.iva(carrinho));
        req.setAttribute("total", CarrinhoService.total(carrinho));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    /** Confirma a compra: desconta o stock, gera a factura, esvazia o carrinho e redirecciona para /factura. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!CarrinhoServlet.exigirCliente(req, resp)) {
            return;
        }
        req.setCharacterEncoding("UTF-8");
        Utilizador cliente = (Utilizador) req.getSession().getAttribute("utilizador");
        Carrinho carrinho = CarrinhoServlet.obterOuCriarCarrinho(req.getSession());

        // 1. Construir os itens actuais do carrinho sem retirar os produtos em caso de falha
        List<VendaItem> linhas = new java.util.ArrayList<>();
        for (Map.Entry<Integer, Double> entrada : carrinho.getQuantidades().entrySet()) {
            Produto p = ProdutoService.procurarPorId(entrada.getKey());
            String nome = (p == null) ? ("Produto #" + entrada.getKey()) : p.getNome();
            double preco = (p == null) ? 0.0 : p.getPreco();
            linhas.add(new VendaItem(entrada.getKey(), nome, entrada.getValue(), preco));
        }

        try {
            // 2. Finalizar a venda de forma atomica e esvaziar o carrinho
            Venda venda = VendaService.finalizar(cliente.getId(), linhas);
            CarrinhoService.esvaziar(carrinho);
            resp.sendRedirect(req.getContextPath() + "/factura?id=" + venda.getId());
        } catch (IllegalStateException | IllegalArgumentException e) {
            // 3. Se faltar stock, manter o carrinho intacto e mostrar o erro na pagina do carrinho
            req.setAttribute("erro", e.getMessage());
            CarrinhoServlet.mostrarCarrinho(req, resp);
        }
    }
}

