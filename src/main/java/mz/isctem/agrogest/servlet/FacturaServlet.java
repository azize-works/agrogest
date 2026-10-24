package mz.isctem.agrogest.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.model.Venda;
import mz.isctem.agrogest.service.VendaService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Visualizacao de uma factura (/factura?id=...).
 * O administrador pode ver qualquer factura; cada cliente so pode ver as suas proprias facturas.
 */
@WebServlet("/factura")
public class FacturaServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/cliente/factura.jsp";

    /** Procura a venda pelo id, valida permissoes (404 ou 403) e abre a pagina da factura. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1. Ler o id da venda; se nao for numero ou nao existir, responder 404
        int id;
        try {
            id = Numeros.lerInt(req.getParameter("id"), "Factura não encontrada.");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Venda venda = VendaService.procurarPorId(id);
        if (venda == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. Verificar se o utilizador autenticado pode ver esta factura (senao responder 403)
        Utilizador autenticado = (Utilizador) req.getSession().getAttribute("utilizador");
        if (!VendaService.podeVer(autenticado, venda)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // 3. Colocar a venda e o cliente dessa venda no pedido e mostrar a factura
        req.setAttribute("venda", venda);
        req.setAttribute("cliente", VendaService.procurarCliente(venda.getClienteId()));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}

