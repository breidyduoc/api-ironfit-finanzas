package cl.duoc.api_ironfit_finanzas.repository;

import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface finanzaRepository extends JpaRepository<finanzaModel, Long> {
    Optional<finanzaModel> findByRutSocio(String rutSocio);

    Optional<finanzaModel> findByEstado(String estado);
}
