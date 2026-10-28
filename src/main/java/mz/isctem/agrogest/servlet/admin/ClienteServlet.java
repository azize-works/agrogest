package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.service.ClienteService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Gestao de clientes (/admin/clientes).
 * GET lista os clientes com compras e total gasto; POST accao=alternar activa/desactiva um cliente.
 */
@WebServlet("/admin/clientes")
public class ClienteServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/clientes.jsp";

    /** Mostra a lista de clientes ordenada pelo parametro "ordem". */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarPagina(req, resp);
    }

    /** Processa a accao de activar/desactivar um cliente. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            // 1. Executar a accao pedida no servico
            if (!"alternar".equals(req.getParameter("accao"))) {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            ClienteService.alternarActivo(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
            // 2. Post/Redirect/Get: redireccionar com aviso de sucesso
            resp.sendRedirect(req.getContextPath() + "/admin/clientes?ok="
                    + URLEncoder.encode("Estado do cliente alterado.", StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // 3. Em caso de erro, voltar a pagina com a mensagem
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Prepara a lista de clientes e abre a JSP. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("clientes", ClienteService.listar(req.getParameter("ordem")));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}

