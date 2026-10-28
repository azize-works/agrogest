package mz.isctem.agrogest.model;

/**
 * Resumo das compras de um cliente (nao e guardado em ficheiro, e calculado a pedido).
 * Junta o cliente, o numero de compras e o total gasto (com IVA) para listagens e relatorios.
 */
public class ResumoCliente {

    private final Utilizador cliente;
    private final int numeroCompras;
    private final double totalGasto;   // soma dos totais das vendas, com IVA

    public ResumoCliente(Utilizador cliente, int numeroCompras, double totalGasto) {
        this.cliente = cliente;
        this.numeroCompras = numeroCompras;
        this.totalGasto = totalGasto;
    }

    public Utilizador getCliente() { return cliente; }

    public int getNumeroCompras() { return numeroCompras; }

    public double getTotalGasto() { return totalGasto; }
}

