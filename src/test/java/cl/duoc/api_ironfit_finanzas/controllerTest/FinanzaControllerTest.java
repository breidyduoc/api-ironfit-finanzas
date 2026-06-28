package cl.duoc.api_ironfit_finanzas.controllerTest;

import cl.duoc.api_ironfit_finanzas.controller.finanzaController;
import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.service.finanzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinanzaControllerTest {

    @Mock
    private finanzaService service;

    @InjectMocks
    private finanzaController controller;

    private finanzaModel pago;

    @BeforeEach
    void a() {
        pago = new finanzaModel();
        pago.setId(1L);
        pago.setRutSocio("12345678-9");
        pago.setMes(6);
        pago.setAnio(2026);
        pago.setMonto(25000.0);
        pago.setEstado("PAGADO");
    }

    // testeo de 200 respuesta ok
    @Test
    @DisplayName("Código 200 - Obtiene todos los pagos")
    void obtenerTodosOk() {
        when(service.obtenerTodosLosPagos()).thenReturn(List.of(pago));

        ResponseEntity<List<finanzaModel>> response = controller.obtenerTodos();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }
}

