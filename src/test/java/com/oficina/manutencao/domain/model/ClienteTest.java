package com.oficina.manutencao.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveNormalizarEAtualizarClientePreservandoDocumento() {
        Cliente cliente = new Cliente(" 123 ", " Ana ", " ANA@OFICINA.COM ", " 11999999999 ");

        Cliente atualizado = cliente.atualizarDados("Ana Silva", "NOVO@OFICINA.COM", "11888888888");

        assertAll(
                () -> assertEquals("123", atualizado.getDocumento()),
                () -> assertEquals("Ana Silva", atualizado.getNome()),
                () -> assertEquals("novo@oficina.com", atualizado.getEmail()),
                () -> assertEquals("11888888888", atualizado.getTelefone())
        );
    }

    @Test
    void deveRejeitarCampoObrigatorioAusente() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("123", "", "ana@oficina.com", "11999999999"));
    }
}
