package mz.isctem.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.service.AuthService;

/**
 * Auto-registo de clientes (/registo).
 * GET mostra o formulario; POST cria a conta e volta ao login.
 */
@WebServlet("/registo")
public class RegistoServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/comum/registo.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            AuthService.registar(
                    req.getParameter("nome"),
                    req.getParameter("email"),
                    req.getParameter("password"),
                    req.getParameter("telefone"),
                    req.getParameter("endereco"));
            // Conta criada: vai para o login com um aviso de sucesso
            resp.sendRedirect(req.getContextPath() + "/login?registado=1");
        } catch (IllegalArgumentException e) {
            // Dado invalido: volta ao formulario com a mensagem (os campos mantem-se)
            req.setAttribute("erro", e.getMessage());
            req.getRequestDispatcher(PAGINA).forward(req, resp);
        }
    }
}
