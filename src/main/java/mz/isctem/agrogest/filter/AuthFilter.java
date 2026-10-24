package mz.isctem.agrogest.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.Utilizador;

/**
 * Controlo de acesso. Corre antes dos Servlets das rotas protegidas:
 *  - sem sessao iniciada -> manda para /login;
 *  - rota /admin/* e utilizador nao admin -> erro 403 (acesso negado).
 */
@WebFilter(urlPatterns = {"/admin/*", "/catalogo", "/carrinho/*", "/compra", "/factura", "/minhas-compras"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // Paginas protegidas nao devem ficar em cache (evita ver a pagina com o botao "voltar" apos sair)
        response.setHeader("Cache-Control", "no-store");

        // getSession(false): nao cria sessao nova, so le a que ja existe
        HttpSession sessao = request.getSession(false);
        Utilizador utilizador = (sessao == null) ? null : (Utilizador) sessao.getAttribute("utilizador");

        if (utilizador == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean rotaAdmin = request.getServletPath().startsWith("/admin");
        if (rotaAdmin && utilizador.getPerfil() != Perfil.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(req, resp);   // tudo certo: segue para o Servlet
    }
}
