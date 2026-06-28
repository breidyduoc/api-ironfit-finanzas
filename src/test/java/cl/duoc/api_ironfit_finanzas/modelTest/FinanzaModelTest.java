package cl.duoc.api_ironfit_finanzas.modelTest;

import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FinanzaModelTest {
    private static Validator valid;

    @BeforeAll
    static void initValidation() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            valid = factory.getValidator();
        }
    }

    // testeo del 200 respuesta ok
    @Test
    @DisplayName("Un pago válido está correcto")
    void pagoValido() {
        finanzaModel p = new finanzaModel();
        p.setRutSocio("12345678-9");
        p.setMes(6);
        p.setAnio(2026);
        p.setMonto(25000.0);
        p.setEstado("PAGADO");

        Set<ConstraintViolation<finanzaModel>> v = valid.validate(p);

        assertTrue(v.isEmpty());
    }

    // testeo del 400 respuesta bad request
    @Test
    @DisplayName("Pago con estado inválido")
    void pagoEstadoInvalido() {
        finanzaModel p = new finanzaModel();
        p.setRutSocio("12345678-9");
        p.setMes(6);
        p.setAnio(2026);
        p.setMonto(25000.0);
        p.setEstado("ERROR");

        Set<ConstraintViolation<finanzaModel>> v = valid.validate(p);

        assertFalse(v.isEmpty());
    }
}