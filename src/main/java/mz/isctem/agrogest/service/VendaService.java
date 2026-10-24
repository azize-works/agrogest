package mz.isctem.agrogest.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.isctem.agrogest.model.Perfil;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.model.Venda;
import mz.isctem.agrogest.model.VendaItem;
import mz.isctem.agrogest.repo.DataStore;

/**
 * Regras de negocio das vendas e facturas: finalizar compra (atomica e sincronizada),
 * listar compras do cliente, procurar venda e verificar permissoes de acesso a factura.
 */
public class VendaService {

    /**
     * Finaliza uma compra de forma atomica ("tudo ou nada").
     * Sincroniza no mesmo monitor de ProdutoService para que reposicoes, remocoes e compras nunca se atropelem.
     */
    public static synchronized Venda finalizar(int clienteId, List<VendaItem> linhas) {
        // 1. Rejeitar se o carrinho estiver vazio
        if (linhas == null || linhas.isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }

        // 2. Bloquear ProdutoService e as listas de produtos e vendas para garantir atomicidade total
        synchronized (ProdutoService.class) {
            DataStore dados = DataStore.getInstance();
            List<Produto> produtos = dados.getProdutos();
            List<Venda> vendas = dados.getVendas();
            synchronized (produtos) {
                synchronized (vendas) {
                    // 3. Verificar o stock de TODOS os itens antes de alterar qualquer quantidade
                    List<String> faltas = new ArrayList<>();
                    for (VendaItem item : linhas) {
                        Produto p = ProdutoService.procurarPorId(item.getProdutoId());
                        if (p == null) {
                            faltas.add(item.getNomeProduto() + " (produto indisponível)");
                        } else if (item.getQuantidade() <= 0 || p.getQuantidade() < item.getQuantidade()) {
                            faltas.add(p.getNome() + " (pedido "
                                    + CarrinhoService.formatarQtd(item.getQuantidade()) + " " + p.getUnidade()
                                    + ", disponível "
                                    + CarrinhoService.formatarQtd(p.getQuantidade()) + " " + p.getUnidade() + ")");
                        }
                    }
                    if (!faltas.isEmpty()) {
                        throw new IllegalStateException("Stock insuficiente: " + String.join("; ", faltas) + ".");
                    }

                    // 4. Descontar o stock de todos os produtos e copiar nome e preco actuais para a factura
                    List<VendaItem> itensFinais = new ArrayList<>();
                    for (VendaItem item : linhas) {
                        Produto p = ProdutoService.procurarPorId(item.getProdutoId());
                        p.setQuantidade(p.getQuantidade() - item.getQuantidade());
                        itensFinais.add(new VendaItem(p.getId(), p.getNome(), item.getQuantidade(), p.getPreco()));
                    }

                    // 5. Gerar o id, a data/hora e o numero da factura (FT-<ano>-<id>) e guardar a venda
                    int idVenda = dados.proximoIdVenda();
                    LocalDateTime agora = LocalDateTime.now();
                    String numeroFactura = "FT-" + agora.getYear() + "-" + idVenda;
                    Venda venda = new Venda(idVenda, clienteId, agora, numeroFactura);
                    venda.setItens(itensFinais);
                    vendas.add(venda);
                    return venda;
                }
            }
        }
    }

    /** Devolve uma copia das vendas de um cliente, ordenada da mais recente para a mais antiga. */
    public static List<Venda> listarPorCliente(int clienteId) {
        List<Venda> lista = DataStore.getInstance().getVendas();
        List<Venda> resultado = new ArrayList<>();
        synchronized (lista) {
            for (Venda v : lista) {
                if (v.getClienteId() == clienteId) {
                    resultado.add(v);
                }
            }
        }
        resultado.sort(Comparator.comparing(Venda::getData).reversed()
                .thenComparing(Comparator.comparingInt(Venda::getId).reversed()));
        return resultado;
    }

    /** Procura uma venda pelo id. Devolve null se nao existir. */
    public static Venda procurarPorId(int id) {
        List<Venda> lista = DataStore.getInstance().getVendas();
        synchronized (lista) {
            for (Venda v : lista) {
                if (v.getId() == id) {
                    return v;
                }
            }
        }
        return null;
    }

    /** Indica se o utilizador autenticado tem permissao para ver a factura (admin ve todas; cliente so as suas). */
    public static boolean podeVer(Utilizador u, Venda v) {
        if (u == null || v == null) {
            return false;
        }
        return u.getPerfil() == Perfil.ADMIN || v.getClienteId() == u.getId();
    }

    /** Procura o utilizador (cliente) de uma venda pelo seu id, para mostrar os dados na factura. */
    public static Utilizador procurarCliente(int clienteId) {
        List<Utilizador> lista = DataStore.getInstance().getUtilizadores();
        synchronized (lista) {
            for (Utilizador u : lista) {
                if (u.getId() == clienteId) {
                    return u;
                }
            }
        }
        return null;
    }
}

