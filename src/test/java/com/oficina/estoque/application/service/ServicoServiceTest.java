package com.oficina.estoque.application.service;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class ServicoServiceTest {
    private final ServicoRepositoryPort repository = mock(ServicoRepositoryPort.class);
    private final ServicoService service = new ServicoService(repository);

    @Test void deveListarServicos() {
        List<Servico> servicos = List.of(new Servico(1, "Troca de oleo", 100));
        when(repository.listarServicos()).thenReturn(servicos);
        assertSame(servicos, service.listarServico());
    }

    @Test void deveCadastrarServico() {
        Servico servico = new Servico(1, "Alinhamento", 80);
        when(repository.salvar("Alinhamento", 80.0)).thenReturn(servico);
        assertSame(servico, service.cadastrarServico("Alinhamento", 80.0));
    }

    @Test void deveAtualizarServico() {
        Servico servico = new Servico(1, "Balanceamento", 90);
        when(repository.atualizarServico(servico)).thenReturn(servico);
        assertSame(servico, service.atualizarServico(servico));
    }

    @Test void deveBuscarServico() {
        Servico servico = new Servico(1, "Revisao", 200);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(servico));
        assertSame(servico, service.buscarServico(1));
        verify(repository, times(1)).buscarPorId(1);
    }

    @Test void deveRemoverServico() {
        Servico servico = new Servico(1, "Revisao", 200);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(servico));
        service.removerServico(1);
        verify(repository).removerServico(1);
    }
}
