package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransicaoStatusServicesTest {
    private final OrdemDeServicoRepositoryPort repository = mock(OrdemDeServicoRepositoryPort.class);
    private final ClienteRepositoryPort clienteRepository = mock(ClienteRepositoryPort.class);
    private final NotificarClientePort notificarCliente = mock(NotificarClientePort.class);

    @Test
    void deveIniciarDiagnosticoComSucesso() {
        IniciarDiagnosticoService service = new IniciarDiagnosticoService(repository);
        OrdemDeServico ordem = criarOrdem(StatusOS.RECEBIDA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        service.iniciarDiagnostico(1);
        
        assertEquals(StatusOS.EM_DIAGNOSTICO, ordem.getStatus());
        verify(repository).salvar(ordem);
    }

    @Test
    void deveLancarExcecaoAoIniciarDiagnosticoComStatusInvalido() {
        IniciarDiagnosticoService service = new IniciarDiagnosticoService(repository);
        OrdemDeServico ordem = criarOrdem(StatusOS.FINALIZADA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        assertThrows(IllegalStateException.class, () -> service.iniciarDiagnostico(1));
    }

    @Test
    void deveEnviarOrcamentoComSucesso() {
        EnviarOrcamentoService service = new EnviarOrcamentoService(repository, clienteRepository, notificarCliente);
        OrdemDeServico ordem = criarOrdem(StatusOS.EM_DIAGNOSTICO);
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "123456789");
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(clienteRepository.buscarPorId("123")).thenReturn(Optional.of(cliente));
        
        service.enviarOrcamento(1);
        
        assertEquals(StatusOS.AGUARDANDO_APROVACAO, ordem.getStatus());
        verify(repository).salvar(ordem);
        verify(notificarCliente).notificarOrcamentoAguardandoAprovacao(cliente, ordem);
    }

    @Test
    void deveLancarExcecaoAoEnviarOrcamentoComStatusInvalido() {
        EnviarOrcamentoService service = new EnviarOrcamentoService(repository, clienteRepository, notificarCliente);
        OrdemDeServico ordem = criarOrdem(StatusOS.RECEBIDA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        assertThrows(IllegalStateException.class, () -> service.enviarOrcamento(1));
    }

    @Test
    void deveFinalizarReparoComSucesso() {
        FinalizarReparoService service = new FinalizarReparoService(repository);
        OrdemDeServico ordem = criarOrdem(StatusOS.EM_EXECUCAO);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        service.finalizarReparo(1);
        
        assertEquals(StatusOS.FINALIZADA, ordem.getStatus());
        verify(repository).salvar(ordem);
    }

    @Test
    void deveLancarExcecaoAoFinalizarReparoComStatusInvalido() {
        FinalizarReparoService service = new FinalizarReparoService(repository);
        OrdemDeServico ordem = criarOrdem(StatusOS.RECEBIDA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        assertThrows(IllegalStateException.class, () -> service.finalizarReparo(1));
    }

    @Test
    void deveEntregarVeiculoComSucesso() {
        EntregarVeiculoService service = new EntregarVeiculoService(repository);
        OrdemDeServico ordem = criarOrdem(StatusOS.FINALIZADA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        service.entregarVeiculo(1);
        
        assertEquals(StatusOS.ENTREGUE, ordem.getStatus());
        verify(repository).salvar(ordem);
    }

    @Test
    void deveLancarExcecaoAoEntregarVeiculoComStatusInvalido() {
        EntregarVeiculoService service = new EntregarVeiculoService(repository);
        OrdemDeServico ordem = criarOrdem(StatusOS.EM_DIAGNOSTICO);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        
        assertThrows(IllegalStateException.class, () -> service.entregarVeiculo(1));
    }

    @Test
    void deveLancarExcecaoQuandoOrdemNaoEncontrada() {
        IniciarDiagnosticoService service = new IniciarDiagnosticoService(repository);
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.iniciarDiagnostico(1));
    }

    private OrdemDeServico criarOrdem(StatusOS status) {
        LocalDateTime agora = LocalDateTime.now();
        return new OrdemDeServico(1, "123", "ABC1234", List.of(), List.of(), 0, status, agora, agora, "Queixa", null);
    }
}
