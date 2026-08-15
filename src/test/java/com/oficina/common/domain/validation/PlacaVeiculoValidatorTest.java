package com.oficina.common.domain.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlacaVeiculoValidatorTest {

    private final PlacaVeiculoValidator validator = new PlacaVeiculoValidator();

    @Test
    void deveRetornarTrueParaValorNuloOuVazio() {
        assertTrue(validator.isValid(null, null));
        assertTrue(validator.isValid("", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ABC-1234",
            "ABC1234",
            "ABC1D23",
            "abc-1234",
            "abc1d23"
    })
    void deveValidarPlacasCorretas(String placa) {
        assertTrue(validator.isValid(placa, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "AB-1234",
            "ABCD-123",
            "ABC-123",
            "ABC-12345",
            "123-ABCD",
            "ABC123A"
    })
    void deveRejeitarPlacasInvalidas(String placa) {
        assertFalse(validator.isValid(placa, null));
    }
}
