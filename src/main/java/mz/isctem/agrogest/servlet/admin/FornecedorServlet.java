package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.TipoFornecedor;
import mz.isctem.agrogest.service.FornecedorService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Gestao de fornecedores (/admin/fornecedores).
 * GET mostra a lista e o formulario; POST cria (accao=criar) ou remove (accao=remover).
 */
@WebServlet("/admin/fornecedores")
public class FornecedorServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/fornecedores.jsp";

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
                FornecedorService.criar(req.getParameter("nome"), req.getParameter("tipo"),
                        req.getParameter("contacto"), req.getParameter("provincia"));
                aviso = "Fornecedor criado.";
            } else if ("remover".equals(accao)) {
                FornecedorService.remover(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Fornecedor removido.";
            } else {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            // Post/Redirect/Get: volta a pagina com o aviso verde
            resp.sendRedirect(req.getContextPath() + "/admin/fornecedores?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // Erro de validacao: mostra a pagina com a mensagem (os campos mantem-se)
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Prepara a lista ordenada e os tipos para o formulario, e abre a JSP. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("fornecedores", FornecedorService.listar(req.getParameter("ordem")));
        req.setAttribute("tipos", TipoFornecedor.values());
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}

