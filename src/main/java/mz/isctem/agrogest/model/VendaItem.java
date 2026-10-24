package mz.isctem.agrogest.model;

import java.io.Serializable;

/** Linha de uma venda. Guarda o nome e o preco no momento da compra. */
public class VendaItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int produtoId;
    private String nomeProduto;  // copia do nome, para a factura nao mudar depois
    private double quantidade;
    private double precoUnit;

    public VendaItem(int produtoId, String nomeProduto, double quantidade, double precoUnit) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnit = precoUnit;
    }

    /** Subtotal da linha = quantidade x preco unitario. */
    public double getSubtotal() {
        return quantidade * precoUnit;
    }

    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public double getQuantidade() { return quantidade; }
    public void setQuantidade(double quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnit() { return precoUnit; }
    public void setPrecoUnit(double precoUnit) { this.precoUnit = precoUnit; }
}
