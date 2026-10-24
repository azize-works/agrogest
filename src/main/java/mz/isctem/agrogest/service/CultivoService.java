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
 * Regras de negocio dos cultivos: listar, criar, remover e procurar.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class CultivoService {

    /**
     * Devolve uma copia dos cultivos ordenada por "cultura" (omissao), "hectares" ou "plantio".
     * Se campanhaId nao for null, devolve apenas os cultivos dessa campanha.
     */
    public static List<Cultivo> listar(String ordem, Integer campanhaId) {
        // 1. Copiar a lista de cultivos em seguranca e aplicar o filtro de campanha
        List<Cultivo> lista = DataStore.getInstance().getCultivos();
        List<Cultivo> resultado = new ArrayList<>();
        synchronized (lista) {
            for (Cultivo cu : lista) {
                if (campanhaId == null || cu.getCampanhaId() == campanhaId) {
                    resultado.add(cu);
                }
            }
        }

        // 2. Ordenar conforme o parametro pedido
        if ("hectares".equals(ordem)) {
            resultado.sort(Comparator.comparingDouble(Cultivo::getHectares));
        } else if ("plantio".equals(ordem)) {
            resultado.sort(Comparator.comparing(Cultivo::getDataPlantio));
        } else {
            resultado.sort(Comparator.comparing(Cultivo::getCultura, String.CASE_INSENSITIVE_ORDER));
        }
        return resultado;
    }

    /** Valida os dados e cria um cultivo numa campanha aberta. Dado invalido -> IllegalArgumentException. */
    public static synchronized Cultivo criar(String campanhaId, String cultura, String variedade,
                                             String hectares, String dataPlantio) {
        // 1. Limpar e validar campos basicos (campanha, cultura, hectares e data de plantio)
        cultura = limpar(cultura);
        variedade = limpar(variedade);
        if (limpar(campanhaId).isEmpty()) {
            throw new IllegalArgumentException("Escolha uma campanha.");
        }
        int idCampanha = Numeros.lerInt(campanhaId, "A campanha escolhida não existe.");
        if (cultura.isEmpty()) {
            throw new IllegalArgumentException("Indique a cultura.");
        }
        double valorHectares = Numeros.lerDouble(hectares, "Indique uma área em hectares válida.");
        if (valorHectares <= 0) {
            throw new IllegalArgumentException("A área em hectares tem de ser maior que zero.");
        }
        LocalDate plantio = lerData(dataPlantio, "Indique uma data de plantio válida (aaaa-mm-dd).");

        // 2. Com as listas bloqueadas, validar que a campanha existe, esta aberta e abrange a data de plantio
        DataStore dados = DataStore.getInstance();
        synchronized (dados.getCampanhas()) {
            synchronized (dados.getCultivos()) {
                Campanha campanha = CampanhaService.procurarPorId(idCampanha);
                if (campanha == null) {
                    throw new IllegalArgumentException("A campanha escolhida não existe.");
                }
                if (!campanha.isAberta()) {
                    throw new IllegalArgumentException("A campanha \"" + campanha.getNome()
                            + "\" está fechada e não aceita novos cultivos.");
                }
                if (plantio.isBefore(campanha.getInicio()) || plantio.isAfter(campanha.getFim())) {
                    throw new IllegalArgumentException("A data de plantio tem de estar dentro do período da campanha ("
                            + campanha.getInicioTexto() + " a " + campanha.getFimTexto() + ").");
                }

                // 3. Criar e guardar o cultivo
                Cultivo novo = new Cultivo(dados.proximoIdCultivo(), idCampanha, cultura,
                        variedade.isEmpty() ? null : variedade, valorHectares, plantio);
                dados.getCultivos().add(novo);
                return novo;
            }
        }
    }

    /** Remove um cultivo se nao tiver colheitas associadas. */
    public static synchronized void remover(int id) {
        DataStore dados = DataStore.getInstance();
        List<Cultivo> cultivos = dados.getCultivos();
        List<Colheita> colheitas = dados.getColheitas();
        synchronized (cultivos) {
            synchronized (colheitas) {
                // 1. Confirmar que o cultivo existe
                Cultivo cu = procurarPorId(id);
                if (cu == null) {
                    throw new IllegalArgumentException("O cultivo não existe.");
                }
                // 2. Impedir a remocao se houver colheitas associadas a este cultivo
                for (Colheita co : colheitas) {
                    if (co.getCultivoId() == id) {
                        throw new IllegalArgumentException("Não é possível remover o cultivo \"" + cu.getCultura()
                                + "\": tem colheitas associadas. Remova primeiro essas colheitas.");
                    }
                }
                // 3. Remover o cultivo
                cultivos.remove(cu);
            }
        }
    }

    /** Procura um cultivo pelo id. Devolve null se nao existir. */
    public static Cultivo procurarPorId(int id) {
        List<Cultivo> lista = DataStore.getInstance().getCultivos();
        synchronized (lista) {
            for (Cultivo cu : lista) {
                if (cu.getId() == id) {
                    return cu;
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

