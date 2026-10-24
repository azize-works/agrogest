package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.model.Campanha;
import mz.isctem.agrogest.service.CampanhaService;
import mz.isctem.agrogest.service.CultivoService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Gestao de cultivos (/admin/cultivos).
 * GET mostra a lista (ordem e filtro por campanha) e o formulario; POST cria ou remove.
 */
@WebServlet("/admin/cultivos")
public class CultivoServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/cultivos.jsp";

    /** Mostra a lista de cultivos e o formulario de criacao. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarPagina(req, resp);
    }

    /** Processa as accoes de criar ou remover um cultivo. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accao = req.getParameter("accao");
        try {
            // 1. Executar a accao pedida no servico
            String aviso;
            if ("criar".equals(accao)) {
                CultivoService.criar(req.getParameter("campanhaId"), req.getParameter("cultura"),
                        req.getParameter("variedade"), req.getParameter("hectares"),
                        req.getParameter("dataPlantio"));
                aviso = "Cultivo criado.";
            } else if ("remover".equals(accao)) {
                CultivoService.remover(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Cultivo removido.";
            } else {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            // 2. Post/Redirect/Get: redireccionar com aviso de sucesso
            resp.sendRedirect(req.getContextPath() + "/admin/cultivos?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // 3. Em caso de erro de validacao, voltar a pagina mantendo o formulario
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Prepara a lista de cultivos, campanhas abertas, todas as campanhas e o mapa id -> nome. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer filtroCampanha = lerFiltroId(req.getParameter("campanha"));
        List<Campanha> todasCampanhas = CampanhaService.listar("nome");

        req.setAttribute("cultivos", CultivoService.listar(req.getParameter("ordem"), filtroCampanha));
        req.setAttribute("filtroCampanha", filtroCampanha);
        req.setAttribute("campanhasAbertas", CampanhaService.listarAbertas());
        req.setAttribute("todasCampanhas", todasCampanhas);
        req.setAttribute("nomesCampanhas", nomesPorId(todasCampanhas));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    /** Constroi um mapa id da campanha -> nome da campanha para mostrar na tabela. */
    static Map<Integer, String> nomesPorId(List<Campanha> campanhas) {
        Map<Integer, String> nomes = new HashMap<>();
        for (Campanha c : campanhas) {
            nomes.put(c.getId(), c.getNome());
        }
        return nomes;
    }

    /** Converte um parametro de filtro opcional em Integer, ou devolve null se vazio/invalido. */
    static Integer lerFiltroId(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

