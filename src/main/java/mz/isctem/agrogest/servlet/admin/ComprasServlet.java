package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.model.Venda;
import mz.isctem.agrogest.service.VendaService;

/**
 * Compras detalhadas (/admin/compras): todas as vendas com cliente, itens, subtotal, IVA e total.
 * Aceita o parametro "ordem" (data, valor ou cliente).
 */
@WebServlet("/admin/compras")
public class ComprasServlet extends HttpServlet {

    /** Prepara a lista de vendas ordenada e o mapa id do cliente -> cliente, e abre a JSP. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Venda> vendas = VendaService.listarTodas(req.getParameter("ordem"));

        // Mapa id do cliente -> cliente, para mostrar nome, endereco e telefone de cada compra
        Map<Integer, Utilizador> clientes = new HashMap<>();
        for (Venda v : vendas) {
            if (!clientes.containsKey(v.getClienteId())) {
                clientes.put(v.getClienteId(), VendaService.procurarCliente(v.getClienteId()));
            }
        }

        req.setAttribute("vendas", vendas);
        req.setAttribute("clientes", clientes);
        req.getRequestDispatcher("/WEB-INF/views/admin/compras.jsp").forward(req, resp);
    }
}

