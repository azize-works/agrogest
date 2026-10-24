package mz.isctem.agrogest.model;

import java.io.Serializable;
import java.time.LocalDate;

/** Produto do catalogo, com stock. Liga-se ao fornecedor pelo seu id. */
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private CategoriaProduto categoria;
    private int fornecedorId;
    private double preco;        // preco por unidade, em MT
    private double quantidade;   // stock disponivel
    private String unidade;      // kg, saco, unidade...
    private String descricao;
    private String fotoUrl;
    private LocalDate validade;

    public Produto(int id, String nome, CategoriaProduto categoria, int fornecedorId,
                   double preco, double quantidade, String unidade,
                   String descricao, String fotoUrl, LocalDate validade) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.fornecedorId = fornecedorId;
        this.preco = preco;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.descricao = descricao;
        this.fotoUrl = fotoUrl;
        this.validade = validade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public CategoriaProduto getCategoria() { return categoria; }
    public void setCategoria(CategoriaProduto categoria) { this.categoria = categoria; }

    public int getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(int fornecedorId) { this.fornecedorId = fornecedorId; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
}
