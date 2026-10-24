package mz.isctem.agrogest.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Campanha agricola, com datas de inicio e fim. Pode ser fechada e reaberta. */
public class Campanha implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private LocalDate inicio;
    private LocalDate fim;
    private boolean aberta = true;

    public Campanha(int id, String nome, LocalDate inicio, LocalDate fim) {
        this.id = id;
        this.nome = nome;
        this.inicio = inicio;
        this.fim = fim;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }

    public LocalDate getFim() { return fim; }
    public void setFim(LocalDate fim) { this.fim = fim; }

    public boolean isAberta() { return aberta; }
    public void setAberta(boolean aberta) { this.aberta = aberta; }

    /** Data de inicio em texto dd/MM/yyyy para as JSP. */
    public String getInicioTexto() {
        return (inicio == null) ? "—" : inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Data de fim em texto dd/MM/yyyy para as JSP. */
    public String getFimTexto() {
        return (fim == null) ? "—" : fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /** Estado da campanha em texto ("Aberta" ou "Fechada") para as JSP. */
    public String getEstadoTexto() {
        return aberta ? "Aberta" : "Fechada";
    }
}
