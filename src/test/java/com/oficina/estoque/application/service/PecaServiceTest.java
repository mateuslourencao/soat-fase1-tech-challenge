package com.oficina.estoque.application.service;
    
import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PecaServiceTest {
    private final PecaRepositoryPort repository = mock(PecaRepositoryPort.class);
    private final PecaService service = new PecaService(repository);

    @Test void deveReporEstoqueDaPeca() {
        Peca peca = new Peca(1, "Filtro de oleo", 35.0, 10);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(peca));
        when(repository.salvar(any(Peca.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Peca pecaReposta = service.reporEstoque(1, 5);

        assertEquals(15, pecaReposta.getQuantidade());
        verify(repository).salvar(pecaReposta);
    }

    @Test void deveLancarExcecaoAoReporEstoqueComQuantidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> service.reporEstoque(1, 0));
        assertThrows(IllegalArgumentException.class, () -> service.reporEstoque(1, -1));
    }

    @Test void deveListarPecas() {
        List<Peca> pecasEsperadas = List.of(new Peca(1, "Filtro de oleo", 35.0, 10));
        when(repository.listarPecas()).thenReturn(pecasEsperadas);
        assertSame(pecasEsperadas, service.listarPecas());
    }

    @Test void deveObterPeca() {
        Peca peca = new Peca(1, "Filtro de oleo", 35.0, 10);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(peca));
        when(repository.salvar(any(Peca.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Peca resultado = service.obterPeca(1, 1);

        assertEquals(9, resultado.getQuantidade());
        verify(repository).salvar(resultado);
    }

    @Test void deveLancarExcecaoAoObterPecaComQuantidadeInvalida() {
        assertThrows(IllegalArgumentException.class, () -> service.obterPeca(1, 0));
        assertThrows(IllegalArgumentException.class, () -> service.obterPeca(1, -1));
    }

    @Test void deveLancarExcecaoAoObterPecaComEstoqueInsuficiente() {
        Peca peca = new Peca(1, "Filtro de oleo", 35.0, 5);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(peca));

        assertThrows(IllegalStateException.class, () -> service.obterPeca(1, 10));
    }

    @Test void deveLancarExcecaoAoObterPecaNaoEncontrada() {
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.obterPeca(1, 1));
    }

    @Test void deveCadastrarPeca() {
        Peca peca = new Peca(1, "Filtro de oleo", 35.0, 10);
        when(repository.salvar(any(Peca.class))).thenReturn(peca);

        Peca resultado = service.cadastrarPeca("Filtro de oleo", 35.0, 10);

        assertSame(peca, resultado);
        verify(repository).salvar(any(Peca.class));
    }

    @Test void deveLancarExcecaoAoCadastrarPecaComDadosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("", 35.0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca(null, 35.0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("Peca", -1.0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("Peca", 35.0, -1));
    }

    @Test void deveAtualizarPeca() {
        Peca peca = new Peca(1, "Filtro de oleo", 35.0, 10);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(peca));
        when(repository.salvar(any(Peca.class))).thenReturn(peca);

        Peca resultado = service.atualizarPeca(peca);

        assertSame(peca, resultado);
        verify(repository).salvar(peca);
    }

    @Test void deveRemoverPeca() {
        Peca peca = new Peca(1, "Filtro de oleo", 35.0, 10);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(peca));

        service.removerPeca(1);

        verify(repository).remover(1);
    }

    @Test void deveLancarExcecaoAoRemoverPecaNaoEncontrada() {
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.removerPeca(1));
    }
}
