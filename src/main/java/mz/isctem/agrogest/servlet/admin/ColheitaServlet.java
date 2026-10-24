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

import mz.isctem.agrogest.model.Colheita;
import mz.isctem.agrogest.model.Cultivo;
import mz.isctem.agrogest.service.CampanhaService;
import mz.isctem.agrogest.service.ColheitaService;
import mz.isctem.agrogest.service.CultivoService;
import mz.isctem.agrogest.util.Numeros;

/**
 * Gestao de colheitas (/admin/colheitas).
 * GET mostra a lista (ordem, filtro por cultivo e totais) e o formulario; POST cria ou remove.
 */
@WebServlet("/admin/colheitas")
public class ColheitaServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/colheitas.jsp";

    /** Mostra a lista de colheitas, os totais e o formulario de criacao. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarPagina(req, resp);
    }

    /** Processa as accoes de criar ou remover uma colheita. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String accao = req.getParameter("accao");
        try {
            // 1. Executar a accao pedida no servico
            String aviso;
            if ("criar".equals(accao)) {
                ColheitaService.criar(req.getParameter("cultivoId"), req.getParameter("data"),
                        req.getParameter("kg"), req.getParameter("precoKg"));
                aviso = "Colheita registada.";
            } else if ("remover".equals(accao)) {
                ColheitaService.remover(Numeros.lerInt(req.getParameter("id"), "Pedido inválido."));
                aviso = "Colheita removida.";
            } else {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            // 2. Post/Redirect/Get: redireccionar com aviso de sucesso
            resp.sendRedirect(req.getContextPath() + "/admin/colheitas?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            // 3. Em caso de erro de validacao, voltar a pagina mantendo o formulario
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Prepara a lista de colheitas, os totais (kg e valor) e o mapa de descricoes dos cultivos. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer filtroCultivo = CultivoServlet.lerFiltroId(req.getParameter("cultivo"));
        List<Colheita> colheitas = ColheitaService.listar(req.getParameter("ordem"), filtroCultivo);
        List<Cultivo> cultivos = CultivoService.listar("cultura", null);
        Map<Integer, String> nomesCampanhas = CultivoServlet.nomesPorId(CampanhaService.listar("nome"));

        req.setAttribute("colheitas", colheitas);
        req.setAttribute("totalKg", ColheitaService.totalKg(colheitas));
        req.setAttribute("totalValor", ColheitaService.totalValor(colheitas));
        req.setAttribute("filtroCultivo", filtroCultivo);
        req.setAttribute("cultivos", cultivos);
        req.setAttribute("descricoesCultivos", descricoesPorId(cultivos, nomesCampanhas));
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }

    /** Constroi um mapa id do cultivo -> "cultura (variedade) - campanha" para mostrar no select e na tabela. */
    private static Map<Integer, String> descricoesPorId(List<Cultivo> cultivos, Map<Integer, String> nomesCampanhas) {
        Map<Integer, String> mapa = new HashMap<>();
        for (Cultivo cu : cultivos) {
            String var = (cu.getVariedade() == null || cu.getVariedade().isEmpty())
                    ? "" : " (" + cu.getVariedade() + ")";
            String camp = nomesCampanhas.getOrDefault(cu.getCampanhaId(), "—");
            mapa.put(cu.getId(), cu.getCultura() + var + " - " + camp);
        }
        return mapa;
    }
}

