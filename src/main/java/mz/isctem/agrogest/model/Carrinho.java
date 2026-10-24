package mz.isctem.agrogest.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carrinho de compras do cliente, guardado na sessao HTTP ("carrinho").
 * Associa cada id de produto a respectiva quantidade escolhida, pela ordem de adicao.
 */
public class Carrinho implements Serializable {
    private static final long serialVersionUID = 1L;

    // Mapa ordenado: produtoId -> quantidade pedida
    private final Map<Integer, Double> quantidades = new LinkedHashMap<>();

    /** Define ou substitui a quantidade de um produto no carrinho. */
    public synchronized void definir(int produtoId, double quantidade) {
        quantidades.put(produtoId, quantidade);
    }

    /** Remove um produto do carrinho. */
    public synchronized void remover(int produtoId) {
        quantidades.remove(produtoId);
    }

    /** Retira todos os produtos do carrinho. */
    public synchronized void esvaziar() {
        quantidades.clear();
    }

    /** Devolve uma copia ordenada do mapa produtoId -> quantidade. */
    public synchronized Map<Integer, Double> getQuantidades() {
        return new LinkedHashMap<>(quantidades);
    }

    /** Indica se o carrinho nao tem nenhum produto. */
    public synchronized boolean isVazio() {
        return quantidades.isEmpty();
    }

    /** Devolve o numero de linhas (produtos distintos) no carrinho. */
    public synchronized int getTotalLinhas() {
        return quantidades.size();
    }
}

