package mz.isctem.agrogest.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import mz.isctem.agrogest.model.Fornecedor;
import mz.isctem.agrogest.model.Produto;
import mz.isctem.agrogest.model.TipoFornecedor;
import mz.isctem.agrogest.repo.DataStore;

/**
 * Regras de negocio dos fornecedores: listar, criar, remover e procurar.
 * Os Servlets so chamam este servico; nao mexem nas listas directamente.
 */
public class FornecedorService {

    /** Devolve uma copia da lista, ordenada por "nome" (por omissao) ou "provincia", sem distinguir maiusculas. */
    public static List<Fornecedor> listar(String ordem) {
        List<Fornecedor> lista = DataStore.getInstance().getFornecedores();
        List<Fornecedor> copia;
        synchronized (lista) {
            copia = new ArrayList<>(lista);
        }
        if ("provincia".equals(ordem)) {
            copia.sort(Comparator.comparing(Fornecedor::getProvincia, String.CASE_INSENSITIVE_ORDER));
        } else {
            copia.sort(Comparator.comparing(Fornecedor::getNome, String.CASE_INSENSITIVE_ORDER));
        }
        return copia;
    }

    /** Valida os dados e cria um fornecedor novo. Dado invalido -> IllegalArgumentException. */
    public static synchronized Fornecedor criar(String nome, String tipo, String contacto, String provincia) {
        nome = limpar(nome);
        contacto = limpar(contacto);
        provincia = limpar(provincia);

        if (nome.isEmpty()) {
            throw new IllegalArgumentException("Indique o nome do fornecedor.");
        }
        TipoFornecedor tipoEnum = lerTipo(tipo);
        if (contacto.isEmpty()) {
            throw new IllegalArgumentException("Indique o contacto do fornecedor.");
        }
        if (provincia.isEmpty()) {
            throw new IllegalArgumentException("Indique a província do fornecedor.");
        }

        DataStore dados = DataStore.getInstance();
        Fornecedor novo = new Fornecedor(dados.proximoIdFornecedor(), nome, tipoEnum, contacto, provincia);
        dados.getFornecedores().add(novo);
        return novo;
    }

    /**
     * Remove um fornecedor. Nao deixa remover se tiver produtos associados.
     * Bloqueia a lista de produtos para ninguem criar um produto deste fornecedor a meio da remocao.
     */
    public static synchronized void remover(int id) {
        DataStore dados = DataStore.getInstance();
        List<Produto> produtos = dados.getProdutos();
        synchronized (produtos) {
            Fornecedor f = procurarPorId(id);
            if (f == null) {
                throw new IllegalArgumentException("O fornecedor não existe.");
            }
            for (Produto p : produtos) {
                if (p.getFornecedorId() == id) {
                    throw new IllegalArgumentException("Não é possível remover \"" + f.getNome()
                            + "\": tem produtos associados. Remova primeiro esses produtos.");
                }
            }
            dados.getFornecedores().remove(f);
        }
    }

    /** Procura um fornecedor pelo id. Devolve null se nao existir. */
    public static Fornecedor procurarPorId(int id) {
        List<Fornecedor> lista = DataStore.getInstance().getFornecedores();
        synchronized (lista) {
            for (Fornecedor f : lista) {
                if (f.getId() == id) {
                    return f;
                }
            }
        }
        return null;
    }

    /** Converte o texto do formulario num TipoFornecedor; texto invalido -> erro. */
    private static TipoFornecedor lerTipo(String tipo) {
        try {
            return TipoFornecedor.valueOf(limpar(tipo));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Escolha um tipo de fornecedor válido.");
        }
    }

    /** Tira espacos das pontas; null passa a texto vazio. */
    private static String limpar(String texto) {
        return (texto == null) ? "" : texto.trim();
    }
}

