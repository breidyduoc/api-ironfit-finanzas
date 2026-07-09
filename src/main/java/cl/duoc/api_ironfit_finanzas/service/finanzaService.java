package cl.duoc.api_ironfit_finanzas.service;

import cl.duoc.api_ironfit_finanzas.dto.estadoFinancieroDTO;
import cl.duoc.api_ironfit_finanzas.dto.finanzaDTO;
import cl.duoc.api_ironfit_finanzas.dto.socioDTO;
import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import cl.duoc.api_ironfit_finanzas.repository.finanzaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class finanzaService {

    private final finanzaRepository repository;
    private final RestTemplate restTemplate;

    private void mapearDtoAModel (finanzaDTO dto, finanzaModel model){
        model.setRutSocio(dto.getRutSocio());
        model.setMes(dto.getMes());
        model.setAnio(dto.getAnio());
        model.setMonto(dto.getMonto());
        model.setEstado(dto.getEstado());
    }

    public List<finanzaModel> obtenerTodosLosPagos() {
        return repository.findAll();
    }

    public Optional<finanzaModel> obtenerPagoPorId(Long id) {
        return repository.findById(id);
    }

    public List<finanzaModel> obtenerHistorialPagos(String rut){
        return repository.findByRutSocioOrderByAnioDescMesDesc(rut);
    }

    public List<finanzaModel> obtenerPagoPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    public List<finanzaModel> obtenerPagosPorMes(Integer mes){

        List<finanzaModel> pagos = repository.findByMes(mes);

        if(pagos.isEmpty()){
            throw new RuntimeException("No existen pagos para el mes indicado.");
        }

        return pagos;
    }

    public List<finanzaModel> obtenerPagosPorAnio(Integer anio){

        List<finanzaModel> pagos = repository.findByAnio(anio);

        if(pagos.isEmpty()){
            throw new RuntimeException("No existen pagos para el año indicado.");
        }

        return pagos;
    }

    public List<finanzaModel> obtenerPagosPorPeriodo(Integer mes, Integer anio) {

        List<finanzaModel> pagos = repository.findByMesAndAnio(mes, anio);

        if (pagos.isEmpty()) {
            throw new RuntimeException("No existen pagos registrados para ese período.");
        }

        return pagos;
    }

    public estadoFinancieroDTO obtenerEstadoFinanciero(String rut){

        List<finanzaModel> pagos = repository.findByRutSocio(rut);

        estadoFinancieroDTO dto = new estadoFinancieroDTO();
        dto.setRut(rut);

        boolean moroso = pagos.stream()
                .anyMatch(p -> p.getEstado().equalsIgnoreCase("MOROSO"));

        boolean pendiente = pagos.stream()
                .anyMatch(p -> p.getEstado().equalsIgnoreCase("PENDIENTE"));

        dto.setPoseeDeuda(moroso || pendiente);

        if (moroso) {
            dto.setEstadoF("MOROSO");
        } else if (pendiente) {
            dto.setEstadoF("PENDIENTE");
        } else {
            dto.setEstadoF("PAGADO");
        }

        return dto;
    }

    @Transactional
    public finanzaModel crearPago(finanzaDTO dto) {

        Optional<finanzaModel> pagoExistente =
                repository.findByRutSocioAndMesAndAnio(
                        dto.getRutSocio(),
                        dto.getMes(),
                        dto.getAnio()
                );

        if (pagoExistente.isPresent()) {
            throw new IllegalStateException(
                    "El socio ya posee un pago registrado para ese mes y año."
            );
        }

        finanzaModel pago = new finanzaModel();
        mapearDtoAModel(dto, pago);

        return repository.save(pago);
    }

    @Transactional
    public Optional<finanzaModel> actualizarPago(Long id, finanzaDTO dto) {
        return repository.findById(id).map(pago -> {
            mapearDtoAModel(dto, pago);
            return repository.save(pago);
        });
    }

    @Transactional
    public Optional<finanzaModel> actualizarEstado(Long id, String nuevoEstado) {
        return repository.findById(id).map(pago -> {
            pago.setEstado(nuevoEstado);
            return repository.save(pago);
        });
    }

    @Transactional
    public boolean borrarPago(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean tieneDeuda(String rut) {

        socioDTO socio = restTemplate.getForObject(
                "http://localhost:21502/api/v4/socios/rut/" + rut,
                socioDTO.class
        );

        if (socio == null) {
            return false;
        }

        List<finanzaModel> pagos = repository.findByRutSocio(rut);

        return socio.getEstado().equalsIgnoreCase("SUSPENDIDO")
                || pagos.stream()
                .anyMatch(p -> p.getEstado().equalsIgnoreCase("MOROSO"));
    }
}