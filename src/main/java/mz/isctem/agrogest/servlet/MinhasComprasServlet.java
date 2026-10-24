package mz.isctem.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.service.VendaService;

/**
 * Historico de compras do cliente autenticado (/minhas-compras).
 * Exclusivo para o perfil CLIENTE (o administrador recebe 403).
 */
@WebServlet("/minhas-compras")
public class MinhasComprasServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/cliente/minhas-compras.jsp";

    /** Lista todas as compras do cliente autenticado, da mais recente para a mais antiga. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!CarrinhoServlet.exigirCliente(req, resp)) {
            return;
        }
        Utilizador cliente = (Utilizador) req.getSession().getAttribute("utilizador");
        req.setAttribute("vendas", VendaService.listarPorCliente(cliente.getId()));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}

