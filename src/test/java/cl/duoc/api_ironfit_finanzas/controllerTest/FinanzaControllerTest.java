package cl.duoc.api_ironfit_finanzas.controllerTest;

import cl.duoc.api_ironfit_finanzas.controller.finanzaController;
import cl.duoc.api_ironfit_finanzas.dto.finanzaDTO;
import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.service.finanzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinanzaControllerTest {

    @Mock
    private finanzaService service;

    @InjectMocks
    private finanzaController controller;

    private finanzaDTO finanzaDTO;
    private finanzaModel finanzaModel;

    @BeforeEach
    void setup() {

        // DTO lo que recibe el controller
        finanzaDTO = new finanzaDTO();
        finanzaDTO.setRutSocio("12345678-9");
        finanzaDTO.setMonto(BigDecimal.valueOf(20000.0));
        finanzaDTO.setEstado("PENDIENTE");

        // MODEL lo que devuelve el service
        finanzaModel = new finanzaModel();
        finanzaModel.setId(1L);
        finanzaModel.setRutSocio("12345678-9");
        finanzaModel.setMonto(BigDecimal.valueOf(20000.0));
        finanzaModel.setEstado("PENDIENTE");
    }

    // 200 OK - GET ALL
    @Test
    void getAll_200() {

        when(service.obtenerTodosLosPagos())
                .thenReturn(List.of(finanzaModel));

        ResponseEntity<List<finanzaModel>> response =
                controller.obtenerTodos();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    // 200 OK - GET BY ID
    @Test
    void getById_200() {

        when(service.obtenerPagoPorId(1L))
                .thenReturn(Optional.of(finanzaModel));

        ResponseEntity<finanzaModel> response =
                controller.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    // 404 NOT FOUND
    @Test
    void getById_404() {

        when(service.obtenerPagoPorId(99L))
                .thenReturn(Optional.empty());

        ResponseEntity<finanzaModel> response =
                controller.obtenerPorId(99L);

        assertEquals(404, response.getStatusCode().value());
    }

    // 201 CREATED
    @Test
    void create_201() {

        when(service.crearPago(any(finanzaDTO.class)))
                .thenReturn(finanzaModel);

        ResponseEntity<finanzaModel> response =
                controller.crearPago(finanzaDTO);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("12345678-9", response.getBody().getRutSocio());
    }

    // 400 BAD REQUEST
    @Test
    void badRequest_400() {

        when(service.crearPago(any(finanzaDTO.class)))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<finanzaModel> response =
                controller.crearPago(finanzaDTO);

        assertEquals(400, response.getStatusCode().value());
    }

    // 500 ERROR
    @Test
    void server_500() {

        when(service.obtenerTodosLosPagos())
                .thenThrow(new RuntimeException("error"));

        ResponseEntity<List<finanzaModel>> response =
                controller.obtenerTodos();

        assertEquals(500, response.getStatusCode().value());
    }
}