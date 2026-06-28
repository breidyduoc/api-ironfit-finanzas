package cl.duoc.api_ironfit_finanzas.modelTest;

import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
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

    // 200 OK (válido)
    @Test
    void finanza_valida() {
        finanzaModel f = new finanzaModel();
        f.setRutSocio("12345678-9");
        f.setMonto(20000.0);
        f.setEstado("PENDIENTE");

        Set<ConstraintViolation<finanzaModel>> v = valid.validate(f);

        assertTrue(v.isEmpty());
    }

    // 400 BAD REQUEST (inválido)
    @Test
    void finanza_invalida() {
        finanzaModel f = new finanzaModel();
        f.setRutSocio(""); // inválido
        f.setMonto(null);

        Set<ConstraintViolation<finanzaModel>> v = valid.validate(f);

        assertFalse(v.isEmpty());
    }
}