package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AprovarOrcamentoServiceTest {
    private final OrdemDeServicoRepositoryPort repository = mock(OrdemDeServicoRepositoryPort.class);
    private final AprovarOrcamentoService service = new AprovarOrcamentoService(repository);

    @Test
    void deveAprovarOrcamentoComSucesso() {
        // Arrange
        int id = 1;
        OrdemDeServico ordem = criarOrdem(StatusOS.AGUARDANDO_APROVACAO);
        when(repository.buscarPorId(id)).thenReturn(Optional.of(ordem));
        when(repository.salvar(any())).thenReturn(ordem);

        // Act
        service.aprovarOrcamento(id);

        // Assert
        assertEquals(StatusOS.EM_EXECUCAO, ordem.getStatus());
        verify(repository).buscarPorId(id);
        verify(repository).salvar(ordem);
    }

    @Test
    void deveLancarExcecaoQuandoOrdemNaoEncontrada() {
        // Arrange
        int id = 1;
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.aprovarOrcamento(id));
        verify(repository).buscarPorId(id);
        verify(repository, never()).salvar(any());
    }

    @Test
    void deveLancarExcecaoQuandoStatusInvalidoParaAprovacao() {
        // Arrange
        int id = 1;
        OrdemDeServico ordem = criarOrdem(StatusOS.RECEBIDA); // Status errado
        when(repository.buscarPorId(id)).thenReturn(Optional.of(ordem));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> service.aprovarOrcamento(id));
        verify(repository).buscarPorId(id);
        verify(repository, never()).salvar(any());
    }

    private OrdemDeServico criarOrdem(StatusOS status) {
        LocalDateTime agora = LocalDateTime.now();

        return OrdemDeServico.builder()
                .id(1)
                .documentoCliente("12345678900")
                .placaVeiculo("ABC-1234")
                .servicos(List.of())
                .pecasNecessarias(List.of())
                .orcamento(150.0)
                .status(status)
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .descricaoQueixas("Problema no motor")
                .diagnosticos(null)
                .build();
    }
}
