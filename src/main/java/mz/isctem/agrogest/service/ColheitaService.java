package mz.isctem.agrogest.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.isctem.agrogest.model.Campanha;
import mz.isctem.agrogest.model.Colheita;
import mz.isctem.agrogest.model.Cultivo;
import mz.isctem.agrogest.repo.DataStore;
import mz.isctem.agrogest.util.Numeros;

/**
 * Regras de negocio das colheitas: listar, criar, remover e calcular totais.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class ColheitaService {

    /**
     * Devolve uma copia das colheitas ordenada por "data" (omissao, mais recente primeiro), "valor" ou "kg".
     * Se cultivoId nao for null, devolve apenas as colheitas desse cultivo.
     */
    public static List<Colheita> listar(String ordem, Integer cultivoId) {
        // 1. Copiar a lista de colheitas em seguranca e aplicar o filtro de cultivo
        List<Colheita> lista = DataStore.getInstance().getColheitas();
        List<Colheita> resultado = new ArrayList<>();
        synchronized (lista) {
            for (Colheita co : lista) {
                if (cultivoId == null || co.getCultivoId() == cultivoId) {
                    resultado.add(co);
                }
            }
        }

        // 2. Ordenar conforme o parametro pedido (data mais recente primeiro por omissao)
        if ("valor".equals(ordem)) {
            resultado.sort(Comparator.comparingDouble(Colheita::getValorTotal));
        } else if ("kg".equals(ordem)) {
            resultado.sort(Comparator.comparingDouble(Colheita::getKg));
        } else {
            resultado.sort(Comparator.comparing(Colheita::getData).reversed());
        }
        return resultado;
    }

    /** Valida os dados e regista uma colheita num cultivo cuja campanha esteja aberta. */
    public static synchronized Colheita criar(String cultivoId, String data, String kg, String precoKg) {
        // 1. Validar e converter os campos do formulario
        if (limpar(cultivoId).isEmpty()) {
            throw new IllegalArgumentException("Escolha um cultivo.");
        }
        int idCultivo = Numeros.lerInt(cultivoId, "O cultivo escolhido não existe.");
        LocalDate dataColheita = lerData(data, "Indique uma data da colheita válida (aaaa-mm-dd).");
        double valorKg = Numeros.lerDouble(kg, "Indique uma quantidade em kg válida.");
        if (valorKg <= 0) {
            throw new IllegalArgumentException("A quantidade em kg tem de ser maior que zero.");
        }
        double valorPrecoKg = Numeros.lerDouble(precoKg, "Indique um preço por kg válido.");
        if (valorPrecoKg <= 0) {
            throw new IllegalArgumentException("O preço por kg tem de ser maior que zero.");
        }

        // 2. Com as listas bloqueadas, validar cultivo, estado da campanha e data >= plantio
        DataStore dados = DataStore.getInstance();
        synchronized (dados.getCampanhas()) {
            synchronized (dados.getCultivos()) {
                synchronized (dados.getColheitas()) {
                    Cultivo cultivo = CultivoService.procurarPorId(idCultivo);
                    if (cultivo == null) {
                        throw new IllegalArgumentException("O cultivo escolhido não existe.");
                    }
                    Campanha campanha = CampanhaService.procurarPorId(cultivo.getCampanhaId());
                    if (campanha == null || !campanha.isAberta()) {
                        throw new IllegalArgumentException("A campanha deste cultivo está fechada e não aceita novas colheitas.");
                    }
                    if (dataColheita.isBefore(cultivo.getDataPlantio())) {
                        throw new IllegalArgumentException("A data da colheita não pode ser anterior à data de plantio ("
                                + cultivo.getDataPlantioTexto() + ").");
                    }

                    // 3. Criar e guardar a colheita
                    Colheita nova = new Colheita(dados.proximoIdColheita(), idCultivo,
                            dataColheita, valorKg, valorPrecoKg);
                    dados.getColheitas().add(nova);
                    return nova;
                }
            }
        }
    }

    /** Remove uma colheita pelo id. Se nao existir, lanca erro. */
    public static synchronized void remover(int id) {
        List<Colheita> lista = DataStore.getInstance().getColheitas();
        synchronized (lista) {
            Colheita alvo = null;
            for (Colheita co : lista) {
                if (co.getId() == id) {
                    alvo = co;
                    break;
                }
            }
            if (alvo == null) {
                throw new IllegalArgumentException("A colheita não existe.");
            }
            lista.remove(alvo);
        }
    }

    /** Soma os quilogramas de todas as colheitas da lista fornecida. */
    public static double totalKg(List<Colheita> lista) {
        double soma = 0;
        for (Colheita co : lista) {
            soma += co.getKg();
        }
        return soma;
    }

    /** Soma o valor total (em MT) de todas as colheitas da lista fornecida. */
    public static double totalValor(List<Colheita> lista) {
        double soma = 0;
        for (Colheita co : lista) {
            soma += co.getValorTotal();
        }
        return soma;
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

