package mz.isctem.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.service.AuthService;

/**
 * Pagina de login (/login).
 * GET mostra o formulario; POST valida email e password e abre a sessao.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/comum/login.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Quem ja entrou nao precisa de ver o login outra vez
        HttpSession sessao = req.getSession(false);
        if (sessao != null && sessao.getAttribute("utilizador") != null) {
            Utilizador u = (Utilizador) sessao.getAttribute("utilizador");
            resp.sendRedirect(req.getContextPath() + destinoInicial(u));
            return;
        }
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Utilizador u = AuthService.autenticar(email, password);
        if (u == null) {
            req.setAttribute("erro", "Email ou palavra-passe incorrectos.");
            req.getRequestDispatcher(PAGINA).forward(req, resp);
            return;
        }

        // Abre a sessao e muda o seu id (protege contra roubo de sessao)
        HttpSession sessao = req.getSession();
        req.changeSessionId();
        sessao.setAttribute("utilizador", u);

        resp.sendRedirect(req.getContextPath() + destinoInicial(u));
    }

    /** Para onde vai cada perfil depois de entrar: admin -> painel, cliente -> catalogo. */
    static String destinoInicial(Utilizador u) {
        return (u.getPerfil() == Perfil.ADMIN) ? "/admin" : "/catalogo";
    }
}
