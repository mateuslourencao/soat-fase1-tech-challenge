package com.oficina.estoque.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstoqueDomainTest {

    @Test void deveCriarEAtualizarPeca() {
        Peca peca = new Peca("Filtro", 30.0, 10);
        assertEquals("Filtro", peca.getDescricao());
        assertEquals(30.0, peca.getValor());
        assertEquals(10, peca.getQuantidade());
        
        peca.atualizarQuantidade(15);
        assertEquals(15, peca.getQuantidade());
    }

    @Test void deveCriarServico() {
        Servico servico = new Servico(1, "Troca de oleo", 100.0);
        assertEquals(1, servico.getId());
        assertEquals("Troca de oleo", servico.getDescricao());
        assertEquals(100.0, servico.getValor());
    }
}
