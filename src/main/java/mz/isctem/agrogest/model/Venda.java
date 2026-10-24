package mz.isctem.agrogest.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Venda (compra de um cliente) com os seus itens e o numero da factura. */
public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Taxa de IVA aplicada nas facturas (16%). */
    public static final double TAXA_IVA = 0.16;

    private int id;
    private int clienteId;
    private LocalDateTime data;
    private String numeroFactura;   // formato FT-ANO-SEQUENCIAL, ex.: FT-2026-1
    private List<VendaItem> itens = new ArrayList<>();

    public Venda(int id, int clienteId, LocalDateTime data, String numeroFactura) {
        this.id = id;
        this.clienteId = clienteId;
        this.data = data;
        this.numeroFactura = numeroFactura;
    }

    /** Soma dos subtotais de todos os itens (sem IVA). */
    public double getSubtotal() {
        double soma = 0;
        for (VendaItem item : itens) {
            soma += item.getSubtotal();
        }
        return soma;
    }

    /** Valor do IVA = subtotal x 16%. */
    public double getIva() {
        return getSubtotal() * TAXA_IVA;
    }

    /** Total a pagar = subtotal + IVA. */
    public double getTotal() {
        return getSubtotal() + getIva();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public List<VendaItem> getItens() { return itens; }
    public void setItens(List<VendaItem> itens) { this.itens = itens; }
}
