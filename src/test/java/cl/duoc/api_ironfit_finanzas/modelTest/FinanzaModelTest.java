package cl.duoc.api_ironfit_finanzas.modelTest;

import cl.duoc.api_ironfit_finanzas.model.finanzaModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FinanzaModelTest {
    private static ValidatorFactory factory;
    private static Validator valid;

    @BeforeAll
    static void initValidation() {
        factory = Validation.buildDefaultValidatorFactory();
        valid = factory.getValidator();
    }

    @AfterAll
    static void cleanup() {
        factory.close();
    }

    // 200 OK (válido)
    @Test
    void finanza_valida() {
        finanzaModel f = new finanzaModel();
        f.setRutSocio("12345678-9");
        f.setMonto(BigDecimal.valueOf(20000.0));
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