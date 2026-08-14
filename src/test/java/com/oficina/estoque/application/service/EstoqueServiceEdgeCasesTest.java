package com.oficina.estoque.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstoqueServiceEdgeCasesTest {

    @Test
    void deveValidarTodosOsFluxosDeErroDePeca() {
        PecaRepositoryPort repository = mock(PecaRepositoryPort.class);
        PecaService service = new PecaService(repository);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("", 1.0, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("Peca", null, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("Peca", -1.0, 1)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarPeca("Peca", 1.0, -1)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.obterPeca(1, 0)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.reporEstoque(1, 0)));

        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.obterPeca(1, 1));
        assertThrows(IllegalArgumentException.class, () -> service.reporEstoque(1, 1));

        when(repository.buscarPorId(2)).thenReturn(Optional.of(new Peca(2, "Filtro", 10, 2)));
        assertThrows(IllegalStateException.class, () -> service.obterPeca(2, 3));
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveExecutarFluxosDeServicoInclusiveBuscaERemocao() {
        ServicoRepositoryPort repository = mock(ServicoRepositoryPort.class);
        ServicoService service = new ServicoService(repository);
        Servico servico = new Servico(7, "Alinhamento", 120.0);

        when(repository.listarServicos()).thenReturn(List.of(servico));
        when(repository.buscarPorId(7)).thenReturn(Optional.of(servico));
        when(repository.atualizarServico(servico)).thenReturn(servico);

        assertEquals(List.of(servico), service.listarServico());
        assertSame(servico, service.buscarServico(7));
        assertSame(servico, service.atualizarServico(servico));
        assertNull(service.removerServico(7));
        verify(repository).deletarServico(7);

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarServico("", 1.0)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarServico("Servico", null)),
                () -> assertThrows(IllegalArgumentException.class, () -> service.cadastrarServico("Servico", -1.0)));
        when(repository.buscarPorId(8)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.buscarServico(8));
        assertThrows(IllegalArgumentException.class, () -> service.removerServico(8));
    }
}
