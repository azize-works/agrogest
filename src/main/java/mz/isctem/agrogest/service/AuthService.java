package mz.isctem.agrogest.service;

import java.util.List;
import java.util.regex.Pattern;

import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.repo.DataStore;
import mz.isctem.agrogest.util.Hash;

/**
 * Regras de autenticacao: registar clientes novos e validar o login.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class AuthService {

    // Formato simples de email: texto@dominio.ext
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    /**
     * Regista um cliente novo. Se algum dado for invalido, lanca
     * IllegalArgumentException com a mensagem a mostrar ao utilizador.
     * E synchronized para dois registos com o mesmo email nao passarem juntos.
     */
    public static synchronized Utilizador registar(String nome, String email, String password,
                                                   String telefone, String endereco) {
        nome = limpar(nome);
        email = limpar(email).toLowerCase();
        telefone = limpar(telefone);
        endereco = limpar(endereco);
        password = (password == null) ? "" : password;

        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Indique o nome.");
        }
        if (!EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("Indique um email válido.");
        }
        if (password.length() < 4) {
            throw new IllegalArgumentException("A palavra-passe deve ter pelo menos 4 caracteres.");
        }
        if (telefone.isEmpty()) {
            throw new IllegalArgumentException("Indique o telefone.");
        }
        if (endereco.isEmpty()) {
            throw new IllegalArgumentException("Indique o endereço.");
        }
        if (procurarPorEmail(email) != null) {
            throw new IllegalArgumentException("Este email já está registado.");
        }

        DataStore dados = DataStore.getInstance();
        Utilizador novo = new Utilizador(dados.proximoIdUtilizador(), nome, email,
                Hash.gerar(password), Perfil.CLIENTE, telefone, endereco);
        dados.getUtilizadores().add(novo);
        return novo;
    }

    /** Valida o login. Devolve o utilizador, ou null se email/password estiverem errados. */
    public static Utilizador autenticar(String email, String password) {
        Utilizador u = procurarPorEmail(limpar(email));
        if (u == null || !u.isAtivo() || password == null) {
            return null;
        }
        return Hash.verificar(password, u.getPasswordHash()) ? u : null;
    }

    /** Procura um utilizador pelo email (sem distinguir maiusculas). Devolve null se nao existir. */
    public static Utilizador procurarPorEmail(String email) {
        List<Utilizador> lista = DataStore.getInstance().getUtilizadores();
        // Uma lista synchronizedList exige synchronized ao percorrer com for
        synchronized (lista) {
            for (Utilizador u : lista) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    return u;
                }
            }
        }
        return null;
    }

    /** Tira espacos das pontas; null passa a texto vazio. */
    private static String limpar(String texto) {
        return (texto == null) ? "" : texto.trim();
    }
}
