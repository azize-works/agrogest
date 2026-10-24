package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.repo.DataStore;

/**
 * Painel do administrador (/admin). Mostra os totais do sistema.
 * Protegido pelo AuthFilter: so o perfil ADMIN chega aqui.
 */
@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DataStore dados = DataStore.getInstance();
        req.setAttribute("totalFornecedores", dados.getFornecedores().size());
        req.setAttribute("totalProdutos", dados.getProdutos().size());
        req.setAttribute("totalCampanhas", dados.getCampanhas().size());
        req.setAttribute("totalVendas", dados.getVendas().size());
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}
