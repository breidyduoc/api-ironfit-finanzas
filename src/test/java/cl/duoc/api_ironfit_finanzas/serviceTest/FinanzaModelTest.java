package cl.duoc.api_ironfit_finanzas.serviceTest;

import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.repository.finanzaRepository;
import cl.duoc.api_ironfit_finanzas.service.finanzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinanzaModelTest {
    @Mock
    private finanzaRepository repository;

    @InjectMocks
    private finanzaService service;

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
    @DisplayName("Obtener todos los pagos desde repository")
    void getAllPagosCorrecto() {
        when(repository.findAll()).thenReturn(List.of(pago));

        List<finanzaModel> result = service.obtenerTodosLosPagos();

        assertEquals(1, result.size());
        assertEquals("12345678-9", result.getFirst().getRutSocio());

        verify(repository).findAll();
    }
}
