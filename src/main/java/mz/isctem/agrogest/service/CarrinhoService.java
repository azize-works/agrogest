package mz.isctem.agrogest.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mz.isctem.agrogest.model.Carrinho;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.model.Venda;
import mz.isctem.agrogest.model.VendaItem;
import mz.isctem.agrogest.util.Numeros;

/**
 * Regras de negocio do carrinho de compras: adicionar, actualizar, remover, esvaziar e calcular totais.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class CarrinhoService {

    /** Adiciona uma quantidade de um produto ao carrinho (somando se ja la estiver), sem exceder o stock. */
    public static void adicionar(Carrinho carrinho, int produtoId, String quantidadeTexto) {
        // 1. Validar que o produto existe e tem stock disponivel
        Produto p = ProdutoService.procurarPorId(produtoId);
        if (p == null) {
            throw new IllegalArgumentException("O produto escolhido não existe.");
        }
        if (p.getQuantidade() <= 0) {
            throw new IllegalArgumentException("O produto \"" + p.getNome() + "\" está esgotado.");
        }

        // 2. Ler e validar a quantidade pedida
        double qtdNova = Numeros.lerDouble(quantidadeTexto, "Indique uma quantidade válida.");
        if (qtdNova <= 0) {
            throw new IllegalArgumentException("A quantidade tem de ser maior que zero.");
        }

        // 3. Somar a quantidade ja existente no carrinho e confirmar que nao ultrapassa o stock
        synchronized (carrinho) {
            Double jaNoCarrinho = carrinho.getQuantidades().get(produtoId);
            double totalPedido = (jaNoCarrinho == null ? 0.0 : jaNoCarrinho) + qtdNova;
            if (totalPedido > p.getQuantidade()) {
                throw new IllegalArgumentException("Stock insuficiente para \"" + p.getNome()
                        + "\": disponível " + formatarQtd(p.getQuantidade()) + " " + p.getUnidade() + ".");
            }
            carrinho.definir(produtoId, totalPedido);
        }
    }

    /** Substitui a quantidade de um produto no carrinho, validando que e > 0 e nao excede o stock. */
    public static void actualizar(Carrinho carrinho, int produtoId, String quantidadeTexto) {
        // 1. Validar que o produto existe
        Produto p = ProdutoService.procurarPorId(produtoId);
        if (p == null) {
            carrinho.remover(produtoId);
            throw new IllegalArgumentException("O produto escolhido já não existe.");
        }

        // 2. Validar a nova quantidade (> 0 e <= stock disponivel)
        double novaQtd = Numeros.lerDouble(quantidadeTexto, "Indique uma quantidade válida.");
        if (novaQtd <= 0) {
            throw new IllegalArgumentException("A quantidade tem de ser maior que zero (use Remover para retirar o item).");
        }
        if (novaQtd > p.getQuantidade()) {
            throw new IllegalArgumentException("Stock insuficiente para \"" + p.getNome()
                    + "\": disponível " + formatarQtd(p.getQuantidade()) + " " + p.getUnidade() + ".");
        }

        // 3. Guardar a nova quantidade no carrinho
        carrinho.definir(produtoId, novaQtd);
    }

    /** Remove um produto do carrinho. */
    public static void remover(Carrinho carrinho, int produtoId) {
        carrinho.remover(produtoId);
    }

    /** Esvazia todo o carrinho. */
    public static void esvaziar(Carrinho carrinho) {
        carrinho.esvaziar();
    }

    /**
     * Constroi a lista de itens do carrinho com o nome e o preco actual de cada produto.
     * Produtos que tenham sido removidos do catalogo sao retirados automaticamente do carrinho.
     */
    public static List<VendaItem> linhas(Carrinho carrinho) {
        List<VendaItem> itens = new ArrayList<>();
        if (carrinho == null) {
            return itens;
        }
        Map<Integer, Double> mapa = carrinho.getQuantidades();
        for (Map.Entry<Integer, Double> entrada : mapa.entrySet()) {
            int produtoId = entrada.getKey();
            Produto p = ProdutoService.procurarPorId(produtoId);
            if (p == null) {
                carrinho.remover(produtoId);
            } else {
                itens.add(new VendaItem(p.getId(), p.getNome(), entrada.getValue(), p.getPreco()));
            }
        }
        return itens;
    }

    /** Calcula o subtotal (sem IVA) dos itens actualmente no carrinho. */
    public static double subtotal(Carrinho carrinho) {
        double soma = 0;
        for (VendaItem item : linhas(carrinho)) {
            soma += item.getSubtotal();
        }
        return soma;
    }

    /** Calcula o valor do IVA (16%) sobre o subtotal do carrinho. */
    public static double iva(Carrinho carrinho) {
        return subtotal(carrinho) * Venda.TAXA_IVA;
    }

    /** Calcula o total a pagar (subtotal + IVA) do carrinho. */
    public static double total(Carrinho carrinho) {
        return subtotal(carrinho) + iva(carrinho);
    }

    /** Formata uma quantidade sem casas decimais desnecessarias para mostrar nas mensagens. */
    static String formatarQtd(double valor) {
        return new DecimalFormat("0.##").format(valor);
    }
}

