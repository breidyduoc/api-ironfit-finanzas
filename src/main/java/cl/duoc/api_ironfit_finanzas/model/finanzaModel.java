package cl.duoc.api_ironfit_finanzas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "PAGO",schema = "ADMIN")
@Data
public class finanzaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGO")
    private Long id;

    @Column(name = "RUT_SOCIO", nullable = false, length = 12)
    @NotBlank(message = "El RUT es obligatorio")
    private String rutSocio;

    @Column(name = "MES", nullable = false)
    @NotNull(message = "El mes es obligatorio")
    @Min(value = 1, message = "Mes inválido")
    @Max(value = 12, message = "Mes inválido")
    private Integer mes;

    @Column(name = "ANIO", nullable = false)
    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @Column(name = "MONTO", nullable = false)
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @Column(name = "ESTADO", nullable = false, length = 20)
    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
            regexp = "PAGADO|PENDIENTE|MOROSO",
            message = "El estado debe ser PAGADO, PENDIENTE o MOROSO"
    )
    private String estado;
}
