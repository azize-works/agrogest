package mz.isctem.agrogest.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mz.isctem.agrogest.model.Carrinho;
import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.service.CarrinhoService;
import mz.isctem.agrogest.service.ProdutoService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Controlador do carrinho de compras (/carrinho/*), exclusivo para o perfil CLIENTE.
 * GET /carrinho mostra o carrinho; POST /adicionar, /actualizar, /remover e /esvaziar alteram o carrinho.
 */
@WebServlet("/carrinho/*")
public class CarrinhoServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/cliente/carrinho.jsp";

    /** Mostra o estado actual do carrinho de compras. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!exigirCliente(req, resp)) {
            return;
        }
        mostrarCarrinho(req, resp);
    }

    /** Processa as accoes do carrinho conforme o caminho (/adicionar, /actualizar, /remover, /esvaziar). */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!exigirCliente(req, resp)) {
            return;
        }
        req.setCharacterEncoding("UTF-8");
        String caminho = req.getPathInfo();
        Carrinho carrinho = obterOuCriarCarrinho(req.getSession());

        // 1. Adicionar a partir do catalogo: redirecciona de volta ao catalogo (ou mostra erro no catalogo)
        if ("/adicionar".equals(caminho)) {
            try {
                int produtoId = Numeros.lerInt(req.getParameter("produtoId"), "Produto inválido.");
                CarrinhoService.adicionar(carrinho, produtoId, req.getParameter("quantidade"));
                String destino = req.getContextPath() + "/catalogo?ok="
                        + URLEncoder.encode("Produto adicionado ao carrinho.", StandardCharsets.UTF_8);
                String cat = req.getParameter("categoria");
                if (cat != null && !cat.trim().isEmpty()) {
                    destino += "&categoria=" + URLEncoder.encode(cat.trim(), StandardCharsets.UTF_8);
                }
                resp.sendRedirect(destino);
            } catch (IllegalArgumentException e) {
                req.setAttribute("erro", e.getMessage());
                CatalogoServlet.mostrarCatalogo(req, resp);
            }
            return;
        }

        // 2. Outras accoes (/actualizar, /remover, /esvaziar): voltam sempre a pagina do carrinho
        try {
            String aviso;
            if ("/actualizar".equals(caminho)) {
                int produtoId = Numeros.lerInt(req.getParameter("produtoId"), "Produto inválido.");
                CarrinhoService.actualizar(carrinho, produtoId, req.getParameter("quantidade"));
                aviso = "Quantidade actualizada.";
            } else if ("/remover".equals(caminho)) {
                int produtoId = Numeros.lerInt(req.getParameter("produtoId"), "Produto inválido.");
                CarrinhoService.remover(carrinho, produtoId);
                aviso = "Item removido do carrinho.";
            } else if ("/esvaziar".equals(caminho)) {
                CarrinhoService.esvaziar(carrinho);
                aviso = "Carrinho esvaziado.";
            } else {
                throw new IllegalArgumentException("Acção desconhecida no carrinho.");
            }
            resp.sendRedirect(req.getContextPath() + "/carrinho?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            mostrarCarrinho(req, resp);
        }
    }

    /** Devolve o carrinho guardado na sessao, criando um novo se ainda nao existir. */
    static Carrinho obterOuCriarCarrinho(HttpSession sessao) {
        synchronized (sessao) {
            Carrinho c = (Carrinho) sessao.getAttribute("carrinho");
            if (c == null) {
                c = new Carrinho();
                sessao.setAttribute("carrinho", c);
            }
            return c;
        }
    }

    /** Prepara as linhas, unidades e totais do carrinho e abre a JSP do carrinho. */
    static void mostrarCarrinho(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Carrinho carrinho = obterOuCriarCarrinho(req.getSession());
        Map<Integer, String> unidades = new HashMap<>();
        for (Produto p : ProdutoService.listar("nome", null)) {
            unidades.put(p.getId(), p.getUnidade());
        }

        req.setAttribute("linhas", CarrinhoService.linhas(carrinho));
        req.setAttribute("unidades", unidades);
        req.setAttribute("subtotal", CarrinhoService.subtotal(carrinho));
        req.setAttribute("iva", CarrinhoService.iva(carrinho));
        req.setAttribute("total", CarrinhoService.total(carrinho));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    /** Garante que o utilizador autenticado tem perfil CLIENTE; caso contrario responde 403. */
    static boolean exigirCliente(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sessao = req.getSession(false);
        Utilizador u = (sessao == null) ? null : (Utilizador) sessao.getAttribute("utilizador");
        if (u == null || u.getPerfil() != Perfil.CLIENTE) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }
}

