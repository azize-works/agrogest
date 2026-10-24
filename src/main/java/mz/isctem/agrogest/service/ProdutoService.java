package mz.isctem.agrogest.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.isctem.agrogest.model.CategoriaProduto;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.repo.DataStore;
import mz.isctem.agrogest.util.Numeros;

/**
 * Regras de negocio dos produtos e do armazem: listar, criar, repor stock, remover e procurar.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class ProdutoService {

    /**
     * Devolve uma copia dos produtos, ordenada por "nome" (omissao), "preco" ou "stock".
     * Se a categoria vier preenchida (ex.: "SEMENTE"), so devolve os dessa categoria.
     */
    public static List<Produto> listar(String ordem, String categoria) {
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : copiaDaLista()) {
            if (categoria == null || categoria.isEmpty() || p.getCategoria().name().equals(categoria)) {
                resultado.add(p);
            }
        }
        ordenar(resultado, ordem);
        return resultado;
    }

    /** Produtos do armazem: so os que tem quantidade > 0, com as mesmas ordens de listar(). */
    public static List<Produto> listarArmazem(String ordem) {
        List<Produto> resultado = new ArrayList<>();
        for (Produto p : copiaDaLista()) {
            if (p.getQuantidade() > 0) {
                resultado.add(p);
            }
        }
        ordenar(resultado, ordem);
        return resultado;
    }

    /**
     * Converte e valida os dados do formulario e cria um produto novo.
     * Obrigatorios: nome, categoria, fornecedor, preco > 0, quantidade >= 0, unidade.
     * Opcionais: descricao, foto (URL http/https) e validade (yyyy-MM-dd).
     */
    public static synchronized Produto criar(String nome, String categoria, String fornecedorId,
                                             String preco, String quantidade, String unidade,
                                             String descricao, String fotoUrl, String validade) {
        nome = limpar(nome);
        unidade = limpar(unidade);
        descricao = limpar(descricao);
        fotoUrl = limpar(fotoUrl);
        validade = limpar(validade);

        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Indique o nome do produto.");
        }
        CategoriaProduto categoriaEnum = lerCategoria(categoria);
        if (limpar(fornecedorId).isEmpty()) {
            throw new IllegalArgumentException("Escolha um fornecedor.");
        }
        int idFornecedor = Numeros.lerInt(fornecedorId, "O fornecedor escolhido não existe.");
        double valorPreco = Numeros.lerDouble(preco, "Indique um preço válido.");
        if (valorPreco <= 0) {
            throw new IllegalArgumentException("O preço tem de ser maior que zero.");
        }
        double valorQuantidade = Numeros.lerDouble(quantidade, "Indique uma quantidade válida.");
        if (valorQuantidade < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }
        if (unidade.isEmpty()) {
            throw new IllegalArgumentException("Indique a unidade (kg, saco, unidade...).");
        }
        if (!fotoUrl.isEmpty() && !fotoUrl.startsWith("http://") && !fotoUrl.startsWith("https://")) {
            throw new IllegalArgumentException("A foto deve ser um endereço que comece por http:// ou https://.");
        }
        LocalDate dataValidade = null;
        if (!validade.isEmpty()) {
            try {
                dataValidade = LocalDate.parse(validade);   // formato yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("A validade deve estar no formato aaaa-mm-dd.");
            }
        }

        DataStore dados = DataStore.getInstance();
        List<Produto> produtos = dados.getProdutos();
        // Verifica o fornecedor e acrescenta com a lista bloqueada (o fornecedor nao pode ser removido entretanto)
        synchronized (produtos) {
            if (FornecedorService.procurarPorId(idFornecedor) == null) {
                throw new IllegalArgumentException("O fornecedor escolhido não existe.");
            }
            Produto novo = new Produto(dados.proximoIdProduto(), nome, categoriaEnum, idFornecedor,
                    valorPreco, valorQuantidade, unidade,
                    descricao.isEmpty() ? null : descricao,
                    fotoUrl.isEmpty() ? null : fotoUrl,
                    dataValidade);
            produtos.add(novo);
            return novo;
        }
    }

    /** Soma a quantidade indicada (tem de ser > 0) ao stock do produto. */
    public static synchronized void reporStock(int id, double quantidadeAAdicionar) {
        if (quantidadeAAdicionar <= 0) {
            throw new IllegalArgumentException("A quantidade a repor tem de ser maior que zero.");
        }
        Produto p = procurarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("O produto não existe.");
        }
        p.setQuantidade(p.getQuantidade() + quantidadeAAdicionar);
    }

    /** Remove um produto pelo id. Se nao existir, erro. */
    public static synchronized void remover(int id) {
        Produto p = procurarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("O produto não existe.");
        }
        DataStore.getInstance().getProdutos().remove(p);
    }

    /** Procura um produto pelo id. Devolve null se nao existir. */
    public static Produto procurarPorId(int id) {
        List<Produto> lista = DataStore.getInstance().getProdutos();
        synchronized (lista) {
            for (Produto p : lista) {
                if (p.getId() == id) {
                    return p;
                }
            }
        }
        return null;
    }

    /** Copia segura da lista de produtos (bloqueada enquanto se copia). */
    private static List<Produto> copiaDaLista() {
        List<Produto> lista = DataStore.getInstance().getProdutos();
        synchronized (lista) {
            return new ArrayList<>(lista);
        }
    }

    /** Ordena por "preco", "stock" ou, por omissao, pelo nome (sem distinguir maiusculas). */
    private static void ordenar(List<Produto> lista, String ordem) {
        if ("preco".equals(ordem)) {
            lista.sort(Comparator.comparingDouble(Produto::getPreco));
        } else if ("stock".equals(ordem)) {
            lista.sort(Comparator.comparingDouble(Produto::getQuantidade));
        } else {
            lista.sort(Comparator.comparing(Produto::getNome, String.CASE_INSENSITIVE_ORDER));
        }
    }

    /** Converte o texto do formulario numa CategoriaProduto; texto invalido -> erro. */
    private static CategoriaProduto lerCategoria(String categoria) {
        try {
            return CategoriaProduto.valueOf(limpar(categoria));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Escolha uma categoria válida.");
        }
    }

    /** Tira espacos das pontas; null passa a texto vazio. */
    private static String limpar(String texto) {
        return (texto == null) ? "" : texto.trim();
    }
}

