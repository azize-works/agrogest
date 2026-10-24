package mz.isctem.agrogest.listener;

import java.io.IOException;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.repo.DataStore;
import mz.isctem.agrogest.repo.Persistencia;
import mz.isctem.agrogest.util.Hash;

/**
 * Corre quando a aplicacao arranca e quando para.
 * Arranque: carrega os dados do ficheiro e garante que existe o administrador.
 * Paragem: grava os dados no ficheiro.
 */
@WebListener
public class AppListener implements ServletContextListener {

    // Administrador criado automaticamente na primeira execucao
    private static final String ADMIN_EMAIL = "admin@agro.mz";
    private static final String ADMIN_PASSWORD = "admin234";

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        try {
            DataStore.setInstance(Persistencia.carregar());
            System.out.println("[AgroGest] Dados carregados.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[AgroGest] Nao foi possivel ler os dados, a iniciar vazio: " + e);
        }
        garantirAdmin();
    }

    @Override
    public void contextDestroyed(ServletContextEvent evento) {
        try {
            Persistencia.guardar(DataStore.getInstance());
            System.out.println("[AgroGest] Dados gravados.");
        } catch (IOException e) {
            System.out.println("[AgroGest] Erro ao gravar os dados: " + e);
        }
    }

    /** Cria o administrador se ainda nao existir nenhum. */
    private void garantirAdmin() {
        DataStore dados = DataStore.getInstance();
        for (Utilizador u : dados.getUtilizadores()) {
            if (u.getPerfil() == Perfil.ADMIN) {
                return;
            }
        }
        dados.getUtilizadores().add(new Utilizador(
                dados.proximoIdUtilizador(), "Administrador", ADMIN_EMAIL,
                Hash.gerar(ADMIN_PASSWORD), Perfil.ADMIN, "", ""));
        System.out.println("[AgroGest] Administrador criado: " + ADMIN_EMAIL);
    }
}
