package com.oficina.administrativo.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuncionarioTest {

    @Test
    void deveNormalizarDadosDoFuncionario() {
        Funcionario funcionario = new Funcionario(1, " Ana ", " ANA@OFICINA.COM ", "hash", PerfilFuncionario.MECANICO, true);

        assertEquals("Ana", funcionario.getNome());
        assertEquals("ana@oficina.com", funcionario.getEmail());
    }

    @Test
    void deveRejeitarDadosInvalidos() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Funcionario(0, null, "ana@oficina.com", null, PerfilFuncionario.MECANICO, true)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Funcionario(0, "Ana", "", null, PerfilFuncionario.MECANICO, true)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Funcionario(0, "Ana", "ana@oficina.com", null, null, true))
        );
    }

    @Test
    void deveAtualizarDadosEPreservarEstado() {
        Funcionario funcionario = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, true);

        Funcionario atualizado = funcionario.atualizarDados("Ana Silva", "NOVO@OFICINA.COM", PerfilFuncionario.ADMIN);

        assertAll(
                () -> assertEquals(1, atualizado.getId()),
                () -> assertEquals("hash", atualizado.getSenhaHash()),
                () -> assertTrue(atualizado.isAtivo()),
                () -> assertEquals("Ana Silva", atualizado.getNome()),
                () -> assertEquals("novo@oficina.com", atualizado.getEmail()),
                () -> assertEquals(PerfilFuncionario.ADMIN, atualizado.getPerfil())
        );
    }

    @Test
    void deveAtivarEInativarFuncionario() {
        Funcionario inativo = new Funcionario(1, "Ana", "ana@oficina.com", "hash", PerfilFuncionario.MECANICO, false);

        Funcionario ativo = inativo.ativar();

        assertTrue(ativo.podeAutenticar());
        assertFalse(ativo.inativar().podeAutenticar());
    }
}
