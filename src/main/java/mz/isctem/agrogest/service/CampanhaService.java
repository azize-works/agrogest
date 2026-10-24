package mz.isctem.agrogest.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.isctem.agrogest.model.Campanha;
import mz.isctem.agrogest.model.Cultivo;
import mz.isctem.agrogest.repo.DataStore;

/**
 * Regras de negocio das campanhas agricolas: listar, criar, fechar, reabrir, remover e procurar.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class CampanhaService {

    /** Devolve uma copia das campanhas ordenada por "nome" (por omissao) ou "inicio". */
    public static List<Campanha> listar(String ordem) {
        List<Campanha> lista = DataStore.getInstance().getCampanhas();
        List<Campanha> copia;
        synchronized (lista) {
            copia = new ArrayList<>(lista);
        }
        if ("inicio".equals(ordem)) {
            copia.sort(Comparator.comparing(Campanha::getInicio));
        } else {
            copia.sort(Comparator.comparing(Campanha::getNome, String.CASE_INSENSITIVE_ORDER));
        }
        return copia;
    }

    /** Devolve apenas as campanhas abertas, ordenadas pelo nome (para o formulario de cultivos). */
    public static List<Campanha> listarAbertas() {
        List<Campanha> abertas = new ArrayList<>();
        for (Campanha c : listar("nome")) {
            if (c.isAberta()) {
                abertas.add(c);
            }
        }
        return abertas;
    }

    /** Valida os dados e cria uma nova campanha aberta. Dado invalido -> IllegalArgumentException. */
    public static synchronized Campanha criar(String nome, String inicio, String fim) {
        // 1. Limpar e validar o nome e as datas de inicio e fim
        nome = limpar(nome);
        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Indique o nome da campanha.");
        }
        LocalDate dataInicio = lerData(inicio, "Indique uma data de início válida (aaaa-mm-dd).");
        LocalDate dataFim = lerData(fim, "Indique uma data de fim válida (aaaa-mm-dd).");
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início.");
        }

        // 2. Verificar se ja existe uma campanha com o mesmo nome (sem distinguir maiusculas)
        DataStore dados = DataStore.getInstance();
        List<Campanha> campanhas = dados.getCampanhas();
        synchronized (campanhas) {
            for (Campanha c : campanhas) {
                if (c.getNome().equalsIgnoreCase(nome)) {
                    throw new IllegalArgumentException("Já existe uma campanha com este nome.");
                }
            }

            // 3. Criar e guardar a campanha (nasce aberta por omissao)
            Campanha nova = new Campanha(dados.proximoIdCampanha(), nome, dataInicio, dataFim);
            campanhas.add(nova);
            return nova;
        }
    }

    /** Fecha uma campanha existente, impedindo novos cultivos e colheitas nela. */
    public static synchronized void fechar(int id) {
        DataStore dados = DataStore.getInstance();
        synchronized (dados.getCampanhas()) {
            Campanha c = procurarPorId(id);
            if (c == null) {
                throw new IllegalArgumentException("A campanha não existe.");
            }
            c.setAberta(false);
        }
    }

    /** Reabre uma campanha fechada, voltando a permitir cultivos e colheitas. */
    public static synchronized void reabrir(int id) {
        DataStore dados = DataStore.getInstance();
        synchronized (dados.getCampanhas()) {
            Campanha c = procurarPorId(id);
            if (c == null) {
                throw new IllegalArgumentException("A campanha não existe.");
            }
            c.setAberta(true);
        }
    }

    /** Remove uma campanha se nao tiver cultivos associados. */
    public static synchronized void remover(int id) {
        DataStore dados = DataStore.getInstance();
        List<Campanha> campanhas = dados.getCampanhas();
        List<Cultivo> cultivos = dados.getCultivos();
        synchronized (campanhas) {
            synchronized (cultivos) {
                // 1. Confirmar que a campanha existe
                Campanha c = procurarPorId(id);
                if (c == null) {
                    throw new IllegalArgumentException("A campanha não existe.");
                }
                // 2. Impedir a remocao se houver cultivos associados a esta campanha
                for (Cultivo cu : cultivos) {
                    if (cu.getCampanhaId() == id) {
                        throw new IllegalArgumentException("Não é possível remover \"" + c.getNome()
                                + "\": tem cultivos associados. Remova primeiro esses cultivos.");
                    }
                }
                // 3. Remover a campanha
                campanhas.remove(c);
            }
        }
    }

    /** Procura uma campanha pelo id. Devolve null se nao existir. */
    public static Campanha procurarPorId(int id) {
        List<Campanha> lista = DataStore.getInstance().getCampanhas();
        synchronized (lista) {
            for (Campanha c : lista) {
                if (c.getId() == id) {
                    return c;
                }
            }
        }
        return null;
    }

    /** Converte texto yyyy-MM-dd numa data LocalDate; se falhar, lanca erro com a mensagem dada. */
    private static LocalDate lerData(String texto, String mensagemErro) {
        String limpo = limpar(texto);
        if (limpo.isEmpty()) {
            throw new IllegalArgumentException(mensagemErro);
        }
        try {
            return LocalDate.parse(limpo);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }

    /** Tira espacos das pontas; null passa a texto vazio. */
    private static String limpar(String texto) {
        return (texto == null) ? "" : texto.trim();
    }
}

