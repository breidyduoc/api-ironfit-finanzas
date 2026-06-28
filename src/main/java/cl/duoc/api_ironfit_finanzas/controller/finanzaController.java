package cl.duoc.api_ironfit_finanzas.controller;

import cl.duoc.api_ironfit_finanzas.dto.finanzaDTO;
import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.service.finanzaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v3/pagos")
public class finanzaController {

    private final finanzaService service;

    @GetMapping
    @Operation(
            summary = "Obtiene todos los pagos",
            description = "Devuelve la lista completa de pagos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            [
                                {
                                    "id": 1,
                                    "rutSocio": "12345678-9",
                                    "mes": 6,
                                    "anio": 2026,
                                    "monto": 25000,
                                    "estado": "PENDIENTE"
                                }
                            ]
                            """))),
            @ApiResponse(responseCode = "204", description = "No existen pagos registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<finanzaModel>> obtenerTodos() {
        List<finanzaModel> pagos = service.obtenerTodosLosPagos();

        if (pagos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pagos);
    }



    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pago por ID",
            description = "Obtiene un pago específico según su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })

    public ResponseEntity<finanzaModel> obtenerPorId(
            @Parameter(
                    description = "ID del pago",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        return service.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/rut/{rut}")
    @Operation(
            summary = "Buscar pago por RUT",
            description = "Obtiene el pago asociado al RUT del socio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<finanzaModel> obtenerPorRut(
            @Parameter(
                    description = "RUT del socio",
                    example = "12345678-9"
            )
            @PathVariable String rut
    ) {
        return service.obtenerPagoPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/estado/{estadoPago}")
    @Operation(
            summary = "Buscar pagos por estado",
            description = "Obtiene un pago filtrado según su estado actual (PAGADO, PENDIENTE o MOROSO)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                        {
                            "id": 1,
                            "rutSocio": "12345678-9",
                            "mes": 6,
                            "anio": 2026,
                            "monto": 25000,
                            "estado": "MOROSO"
                        }
                        """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron pagos con ese estado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                        {
                            "mensaje": "No existe un pago con el estado solicitado"
                        }
                        """)
                    )
            )
    })
    public ResponseEntity<finanzaModel> obtenerPorEstado(
            @Parameter(
                    description = "Estado del pago",
                    example = "MOROSO"
            )
            @PathVariable String estadoPago
    ) {
        return service.obtenerPagoPorEstado(estadoPago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del pago a registrar",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                {
                    "rutSocio": "12345678-9",
                    "mes": 6,
                    "anio": 2026,
                    "monto": 25000,
                    "estado": "PENDIENTE"
                }
                """)))
    @Operation(
            summary = "Registrar un nuevo pago",
            description = "Crea un nuevo registro de pago para un socio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<finanzaModel> crearPago(
            @Valid @RequestBody finanzaDTO dto
    ) {
        finanzaModel nuevoPago = service.crearPago(dto);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }



    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del pago a actualizar",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                {
                    "rutSocio": "12345678-9",
                    "mes": 7,
                    "anio": 2026,
                    "monto": 30000,
                    "estado": "PAGADO"
                }
                """)))
    @Operation(
            summary = "Actualizar un pago",
            description = "Actualiza completamente un pago mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<finanzaModel> actualizarPago(
            @Parameter(
                    description = "ID del pago",
                    example = "1"
            )
            @PathVariable Long id,
            @Valid @RequestBody finanzaDTO dto
    ) {
        return service.actualizarPago(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar estado del pago",
            description = "Modifica únicamente el estado del pago"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    public ResponseEntity<finanzaModel> actualizarEstado(
            @Parameter(
                    description = "ID del pago",
                    example = "1"
            )
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        String nuevoEstado = request.get("estadoPago");

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return service.actualizarEstado(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un pago",
            description = "Elimina un pago del sistema mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> eliminarPago(
            @Parameter(
                    description = "ID del pago",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        if (service.borrarPago(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }



    @GetMapping("/deuda/{rut}")
    @Operation(
            summary = "Verificar deuda de un socio",
            description = "Consulta si un socio posee deuda vigente para permitir o restringir su acceso"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = "true"),
                                    @ExampleObject(value = "false")
                            }))
    })
    public ResponseEntity<Boolean> verificarDeuda(
            @Parameter(
                    description = "RUT del socio",
                    example = "12345678-9"
            )
            @PathVariable String rut
    ) {
        return ResponseEntity.ok(service.tieneDeuda(rut));
    }
}
