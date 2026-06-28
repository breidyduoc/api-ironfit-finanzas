package cl.duoc.api_ironfit_finanzas.serviceTest;

import cl.duoc.api_ironfit_finanzas.dto.finanzaDTO;
import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.repository.finanzaRepository;
import cl.duoc.api_ironfit_finanzas.service.finanzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinanzaServiceTest {

    @Mock
    private finanzaRepository repository;

    @InjectMocks
    private finanzaService service;

    private finanzaDTO finanzaDTO;
    private finanzaModel finanzaModel;

    @BeforeEach
    void setup() {

        // DTO entrada service
        finanzaDTO = new finanzaDTO();
        finanzaDTO.setRutSocio("12345678-9");
        finanzaDTO.setMonto(20000.0);
        finanzaDTO.setEstado("PENDIENTE");

        // MODEL entrada repository
        finanzaModel = new finanzaModel();
        finanzaModel.setId(1L);
        finanzaModel.setRutSocio("12345678-9");
        finanzaModel.setMonto(20000.0);
        finanzaModel.setEstado("PENDIENTE");
    }

    // GET ALL - 200 OK
    @Test
    void getAll_ok() {

        when(repository.findAll()).thenReturn(List.of(finanzaModel));

        List<finanzaModel> result = service.obtenerTodosLosPagos();

        assertEquals(1, result.size());
    }

    // GET BY ID - 200 OK
    @Test
    void getById_ok() {

        when(repository.findById(1L)).thenReturn(Optional.of(finanzaModel));

        Optional<finanzaModel> result = service.obtenerPagoPorId(1L);

        assertTrue(result.isPresent());
    }

    // GET BY ID - 404 NOT FOUND
    @Test
    void getById_404() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<finanzaModel> result = service.obtenerPagoPorId(99L);

        assertTrue(result.isEmpty());
    }

    // CREATE - 201 OK
    @Test
    void create_201() {

        when(repository.save(any(finanzaModel.class)))
                .thenReturn(finanzaModel);

        finanzaModel result = service.crearPago(finanzaDTO);

        assertNotNull(result);
        assertEquals("12345678-9", result.getRutSocio());
        assertEquals(20000.0, result.getMonto());
    }

    // ERROR - 500
    @Test
    void server_500() {

        when(repository.findAll())
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> service.obtenerTodosLosPagos());
    }
}