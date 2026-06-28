package cl.duoc.api_ironfit_finanzas.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class finanzaDTO {

    @NotBlank(message = "El RUT es obligatorio")
    private String rutSocio;

    @NotNull(message = "El mes es obligatorio")
    private Integer mes;

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @NotNull(message = "El monto es obligatorio")
    private Double monto;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}