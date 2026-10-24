package mz.isctem.agrogest.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Colheita de um cultivo: data, quantidade em kg e preco por kg. */
public class Colheita implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int cultivoId;       // cultivo a que pertence
    private LocalDate data;
    private double kg;
    private double precoKg;

    public Colheita(int id, int cultivoId, LocalDate data, double kg, double precoKg) {
        this.id = id;
        this.cultivoId = cultivoId;
        this.data = data;
        this.kg = kg;
        this.precoKg = precoKg;
    }

    /** Valor total da colheita = kg x preco por kg (calculado, nao guardado). */
    public double getValorTotal() {
        return kg * precoKg;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCultivoId() { return cultivoId; }
    public void setCultivoId(int cultivoId) { this.cultivoId = cultivoId; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public double getKg() { return kg; }
    public void setKg(double kg) { this.kg = kg; }

    public double getPrecoKg() { return precoKg; }
    public void setPrecoKg(double precoKg) { this.precoKg = precoKg; }

    /** Data da colheita em texto dd/MM/yyyy para as JSP. */
    public String getDataTexto() {
        return (data == null) ? "—" : data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
