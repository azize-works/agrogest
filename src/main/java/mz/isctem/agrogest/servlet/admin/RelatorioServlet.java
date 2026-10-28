package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.ResumoCliente;
import mz.isctem.agrogest.service.RelatorioService;

/**
 * Relatorios de clientes (/admin/relatorios).
 * Parametro "tipo": nome (omissao), compraram, assiduos ou semcompras.
 */
@WebServlet("/admin/relatorios")
public class RelatorioServlet extends HttpServlet {

    /** Escolhe o relatorio pedido, prepara a lista e o titulo, e abre a JSP. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String tipo = req.getParameter("tipo");
        List<ResumoCliente> linhas;
        String titulo;
        boolean ranking = false;   // true = mostrar a coluna da posicao

        // 1. Escolher o relatorio (tipo desconhecido = por nome)
        if ("compraram".equals(tipo)) {
            linhas = RelatorioService.clientesQueCompraram();
            titulo = "Clientes que compraram (por volume de compras)";
            ranking = true;
        } else if ("assiduos".equals(tipo)) {
            linhas = RelatorioService.clientesMaisAssiduos();
            titulo = "Clientes mais assíduos (por número de compras)";
            ranking = true;
        } else if ("semcompras".equals(tipo)) {
            linhas = RelatorioService.clientesSemCompras();
            titulo = "Clientes sem compras";
        } else {
            tipo = "nome";
            linhas = RelatorioService.clientesPorNome();
            titulo = "Todos os clientes (por nome)";
        }

        // 2. Enviar para a JSP
        req.setAttribute("tipo", tipo);
        req.setAttribute("titulo", titulo);
        req.setAttribute("ranking", ranking);
        req.setAttribute("linhas", linhas);
        req.getRequestDispatcher("/WEB-INF/views/admin/relatorios.jsp").forward(req, resp);
    }
}

