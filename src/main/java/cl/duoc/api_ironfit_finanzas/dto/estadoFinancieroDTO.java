package cl.duoc.api_ironfit_finanzas.dto;

import lombok.Data;

@Data
public class estadoFinancieroDTO {
    private String rut;
    private boolean poseeDeuda;
    private String estado;
}