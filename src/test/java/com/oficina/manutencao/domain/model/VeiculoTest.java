package com.oficina.manutencao.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoTest {

    @Test
    void deveNormalizarEAtualizarVeiculoPreservandoPlaca() {
        Veiculo veiculo = new Veiculo(" abc1234 ", " Toyota ", " Corolla ", 2020);

        Veiculo atualizado = veiculo.atualizarDados("Toyota", "Corolla Cross", 2022);

        assertAll(
                () -> assertEquals("ABC1234", atualizado.getPlaca()),
                () -> assertEquals("Corolla Cross", atualizado.getModelo()),
                () -> assertEquals(2022, atualizado.getAno())
        );
    }

    @Test
    void deveRejeitarDadosInvalidos() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Veiculo("", "Toyota", "Corolla", 2020)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Veiculo("ABC1234", "Toyota", "Corolla", 0))
        );
    }
}
