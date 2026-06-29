package cl.duoc.api_ironfit_finanzas.service;

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

    public Optional<finanzaModel> obtenerPagoPorRut(String rut) {
        return repository.findByRutSocio(rut);
    }

    public List<finanzaModel> obtenerPagoPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    @Transactional
    public finanzaModel crearPago(finanzaDTO dto) {
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
                "http://localhost:21502/api/v3/socios/rut/" + rut,
                socioDTO.class
        );

        if (socio == null) {
            return false;
        }

        Optional<finanzaModel> pago = repository.findByRutSocio(rut);

        return socio.getEstado().equalsIgnoreCase("SUSPENDIDO")
                || (pago.isPresent()
                && pago.get().getEstado().equalsIgnoreCase("MOROSO"));
    }
}