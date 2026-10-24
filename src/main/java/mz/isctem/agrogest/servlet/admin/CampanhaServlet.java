package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.service.CampanhaService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Gestao de campanhas agricolas (/admin/campanhas).
 * GET mostra a lista e o formulario; POST cria, fecha, reabre ou remove uma campanha.
 */
@WebServlet("/admin/campanhas")
public class CampanhaServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/campanhas.jsp";

    /** Mostra a lista de campanhas e o formulario de criacao. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarPagina(req, resp);
    }

    /** Processa as accoes de criar, fechar, reabrir ou remover uma campanha. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accao = req.getParameter("accao");
        try {
            // 1. Executar a accao pedida no servico
            String aviso;
            if ("criar".equals(accao)) {
                CampanhaService.criar(req.getParameter("nome"),
                        req.getParameter("inicio"), req.getParameter("fim"));
                aviso = "Campanha criada.";
            } else if ("fechar".equals(accao)) {
                CampanhaService.fechar(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Campanha fechada.";
            } else if ("reabrir".equals(accao)) {
                CampanhaService.reabrir(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Campanha reaberta.";
            } else if ("remover".equals(accao)) {
                CampanhaService.remover(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Campanha removida.";
            } else {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            // 2. Post/Redirect/Get: redireccionar com aviso de sucesso
            resp.sendRedirect(req.getContextPath() + "/admin/campanhas?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // 3. Em caso de erro de validacao, voltar a pagina mantendo o formulario
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Prepara a lista ordenada de campanhas e abre a JSP. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("campanhas", CampanhaService.listar(req.getParameter("ordem")));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}

