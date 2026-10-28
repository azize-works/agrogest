package mz.isctem.agrogest.servlet.admin;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import mz.isctem.agrogest.repo.DataStore;
import mz.isctem.agrogest.repo.Persistencia;

/**
 * Persistencia manual (/admin/persistencia).
 * GET mostra o estado do ficheiro; POST accao=gravar grava agora e accao=eliminar apaga so o ficheiro.
 */
@WebServlet("/admin/persistencia")
public class PersistenciaServlet extends HttpServlet {

    private static final String PAGINA = "/WEB-INF/views/admin/persistencia.jsp";

    /** Mostra o estado do ficheiro de dados. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        mostrarPagina(req, resp);
    }

    /** Grava os dados em ficheiro ou elimina o ficheiro, conforme a accao. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String accao = req.getParameter("accao");
        try {
            // 1. Executar a accao pedida (os dados em memoria nunca sao apagados)
            String aviso;
            if ("gravar".equals(accao)) {
                Persistencia.guardar(DataStore.getInstance());
                aviso = "Dados gravados no ficheiro.";
            } else if ("eliminar".equals(accao)) {
                Persistencia.eliminar();
                aviso = "Ficheiro de dados eliminado (os dados em memória mantêm-se).";
            } else {
                throw new IllegalArgumentException("Acção desconhecida.");
            }
            // 2. Post/Redirect/Get: redireccionar com aviso de sucesso
            resp.sendRedirect(req.getContextPath() + "/admin/persistencia?ok="
                    + URLEncoder.encode(aviso, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // 3. Erro de leitura/escrita no disco: mostrar a mensagem na pagina
            req.setAttribute("erro", "Erro ao aceder ao ficheiro de dados: " + e.getMessage());
            mostrarPagina(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("erro", e.getMessage());
            mostrarPagina(req, resp);
        }
    }

    /** Coloca no pedido o estado do ficheiro (existe, caminho, tamanho, ultima gravacao) e abre a JSP. */
    private void mostrarPagina(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("existe", Persistencia.existeFicheiro());
        req.setAttribute("caminho", Persistencia.getCaminho());
        req.setAttribute("tamanho", Persistencia.getTamanho());
        req.setAttribute("ultimaModificacao", Persistencia.getUltimaModificacao());
        req.getRequestDispatcher(PAGINA).forward(req, resp);
    }
}

