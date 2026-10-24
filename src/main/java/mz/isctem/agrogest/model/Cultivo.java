package mz.isctem.agrogest.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Cultivo registado dentro de uma campanha (ex.: Feijao nhemba, 8 ha). */
public class Cultivo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int campanhaId;      // campanha a que pertence
    private String cultura;
    private String variedade;
    private double hectares;
    private LocalDate dataPlantio;

    public Cultivo(int id, int campanhaId, String cultura, String variedade,
                   double hectares, LocalDate dataPlantio) {
        this.id = id;
        this.campanhaId = campanhaId;
        this.cultura = cultura;
        this.variedade = variedade;
        this.hectares = hectares;
        this.dataPlantio = dataPlantio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCampanhaId() { return campanhaId; }
    public void setCampanhaId(int campanhaId) { this.campanhaId = campanhaId; }

    public String getCultura() { return cultura; }
    public void setCultura(String cultura) { this.cultura = cultura; }

    public String getVariedade() { return variedade; }
    public void setVariedade(String variedade) { this.variedade = variedade; }

    public double getHectares() { return hectares; }
    public void setHectares(double hectares) { this.hectares = hectares; }

    public LocalDate getDataPlantio() { return dataPlantio; }
    public void setDataPlantio(LocalDate dataPlantio) { this.dataPlantio = dataPlantio; }

    /** Data de plantio em texto dd/MM/yyyy para as JSP. */
    public String getDataPlantioTexto() {
        return (dataPlantio == null) ? "—" : dataPlantio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
