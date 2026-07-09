package cl.duoc.api_ironfit_finanzas.controllerTest;

import cl.duoc.api_ironfit_finanzas.controller.finanzaController;
import cl.duoc.api_ironfit_finanzas.dto.estadoFinancieroDTO;
import cl.duoc.api_ironfit_finanzas.dto.finanzaDTO;
import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.service.finanzaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
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
    private estadoFinancieroDTO estado;
    private finanzaModel finanzaModel;

    @BeforeEach
    void setup() {

        // DTO lo que recibe el controller
        finanzaDTO = new finanzaDTO();
        finanzaDTO.setRutSocio("12345678-9");
        finanzaDTO.setMonto(BigDecimal.valueOf(20000.0));
        finanzaDTO.setEstado("PENDIENTE");
        finanzaDTO.setMes(7);
        finanzaDTO.setAnio(2026);

        // DTO lo que recibe estado
        estado = new estadoFinancieroDTO();
        estado.setRut("12345678-9");
        estado.setPoseeDeuda(true);
        estado.setEstado("MOROSO");

        // MODEL lo que devuelve el service
        finanzaModel = new finanzaModel();
        finanzaModel.setId(1L);
        finanzaModel.setRutSocio("12345678-9");
        finanzaModel.setMonto(BigDecimal.valueOf(20000.0));
        finanzaModel.setEstado("PENDIENTE");
        finanzaModel.setMes(7);
        finanzaModel.setAnio(2026);
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

    @Test
    void getByMes_200(){

        when(service.obtenerPagosPorMes(7))
                .thenReturn(List.of(finanzaModel));

        ResponseEntity<List<finanzaModel>> response =
                controller.obtenerPorMes(7);

        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void getByAnio_200(){

        when(service.obtenerPagosPorAnio(2026))
                .thenReturn(List.of(finanzaModel));

        ResponseEntity<List<finanzaModel>> response =
                controller.obtenerPorAnio(2026);

        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void getHistorial_200(){

        when(service.obtenerHistorialPagos("12345678-9"))
                .thenReturn(List.of(finanzaModel));

        ResponseEntity<List<finanzaModel>> response =
                controller.obtenerHistorial("12345678-9");

        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void getPeriodo_200(){

        when(service.obtenerPagosPorPeriodo(7,2026))
                .thenReturn(List.of(finanzaModel));

        ResponseEntity<List<finanzaModel>> response =
                controller.obtenerPorPeriodo(7,2026);

        assertEquals(200,response.getStatusCode().value());
    }

    @Test
    void obtenerEstadoFinancieroOk(){

        when(service.obtenerEstadoFinanciero("12345678-9"))
                .thenReturn(estado);


        ResponseEntity<estadoFinancieroDTO> response =
                controller.estadoFinanciero("12345678-9");


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertTrue(
                response.getBody().isPoseeDeuda()
        );
    }


}