package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.service.FornecedorService;
import mz.isctem.agrogest.service.ProdutoService;

/**
 * Armazem (/admin/armazem): lista so os produtos com stock > 0.
 * Aceita o parametro "ordem" (nome, preco ou stock).
 */
@WebServlet("/admin/armazem")
public class ArmazemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("produtos", ProdutoService.listarArmazem(req.getParameter("ordem")));
        req.setAttribute("nomesFornecedores", ProdutoServlet.nomesPorId(FornecedorService.listar("nome")));
        req.getRequestDispatcher("/WEB-INF/views/admin/armazem.jsp").forward(req, resp);
    }
}

