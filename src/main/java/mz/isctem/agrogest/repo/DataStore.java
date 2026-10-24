package mz.isctem.agrogest.repo;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import mz.isctem.agrogest.model.Campanha;
import mz.isctem.agrogest.model.Colheita;
import mz.isctem.agrogest.model.Cultivo;
import mz.isctem.agrogest.model.Fornecedor;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.model.Utilizador;
import mz.isctem.agrogest.model.Venda;

/**
 * Base de dados em memoria: guarda todas as listas e os contadores de IDs.
 * E esta classe que e gravada em ficheiro e lida no arranque.
 * Existe uma unica instancia (singleton) partilhada por toda a aplicacao.
 */
public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;

    // Listas sincronizadas (synchronizedList): varios pedidos podem usa-las ao mesmo tempo
    private final List<Utilizador> utilizadores = Collections.synchronizedList(new ArrayList<>());
    private final List<Fornecedor> fornecedores = Collections.synchronizedList(new ArrayList<>());
    private final List<Produto> produtos = Collections.synchronizedList(new ArrayList<>());
    private final List<Campanha> campanhas = Collections.synchronizedList(new ArrayList<>());
    private final List<Cultivo> cultivos = Collections.synchronizedList(new ArrayList<>());
    private final List<Colheita> colheitas = Collections.synchronizedList(new ArrayList<>());
    private final List<Venda> vendas = Collections.synchronizedList(new ArrayList<>());

    // Contadores de IDs (gravados junto, para os IDs nao repetirem apos reiniciar)
    private final AtomicInteger seqUtilizador = new AtomicInteger();
    private final AtomicInteger seqFornecedor = new AtomicInteger();
    private final AtomicInteger seqProduto = new AtomicInteger();
    private final AtomicInteger seqCampanha = new AtomicInteger();
    private final AtomicInteger seqCultivo = new AtomicInteger();
    private final AtomicInteger seqColheita = new AtomicInteger();
    private final AtomicInteger seqVenda = new AtomicInteger();

    // Instancia unica (nao e gravada em ficheiro)
    private static DataStore instancia = new DataStore();

    /** Devolve a base de dados em uso. */
    public static DataStore getInstance() {
        return instancia;
    }

    /** Substitui a base de dados (usado ao carregar do ficheiro no arranque). */
    public static void setInstance(DataStore carregada) {
        instancia = carregada;
    }

    // --- Listas ---
    public List<Utilizador> getUtilizadores() { return utilizadores; }
    public List<Fornecedor> getFornecedores() { return fornecedores; }
    public List<Produto> getProdutos() { return produtos; }
    public List<Campanha> getCampanhas() { return campanhas; }
    public List<Cultivo> getCultivos() { return cultivos; }
    public List<Colheita> getColheitas() { return colheitas; }
    public List<Venda> getVendas() { return vendas; }

    // --- Proximo ID de cada entidade (comeca em 1) ---
    public int proximoIdUtilizador() { return seqUtilizador.incrementAndGet(); }
    public int proximoIdFornecedor() { return seqFornecedor.incrementAndGet(); }
    public int proximoIdProduto() { return seqProduto.incrementAndGet(); }
    public int proximoIdCampanha() { return seqCampanha.incrementAndGet(); }
    public int proximoIdCultivo() { return seqCultivo.incrementAndGet(); }
    public int proximoIdColheita() { return seqColheita.incrementAndGet(); }
    public int proximoIdVenda() { return seqVenda.incrementAndGet(); }
}
