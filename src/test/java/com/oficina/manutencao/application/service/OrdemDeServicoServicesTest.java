package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdemDeServicoServicesTest {
    private final OrdemDeServicoRepositoryPort repository = mock(OrdemDeServicoRepositoryPort.class);
    private final PecaRepositoryPort pecaRepository = mock(PecaRepositoryPort.class);
    private final ServicoRepositoryPort servicoRepository = mock(ServicoRepositoryPort.class);
    private final ClienteRepositoryPort clienteRepository = mock(ClienteRepositoryPort.class);
    private final NotificarClientePort notificarCliente = mock(NotificarClientePort.class);

    @Test void deveCadastrarOrdemDeServico() {
        OrdemDeServico ordem = ordem(StatusOS.RECEBIDA);
        when(repository.salvar(ordem)).thenReturn(ordem);
        assertSame(ordem, new CadastrarOrdemDeServicoService(repository).cadastrarOrdemDeServico(ordem));
    }

    @Test void deveBuscarOrdemDeServico() {
        OrdemDeServico ordem = ordem(StatusOS.RECEBIDA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        assertSame(ordem, new BuscarOrdemDeServicoService(repository).buscarOrdemDeServico(1));
    }

    @Test void deveListarOrdensDeServico() {
        List<OrdemDeServico> ordens = List.of(ordem(StatusOS.RECEBIDA));
        when(repository.listarTodos()).thenReturn(ordens);
        assertSame(ordens, new ListarOrdensDeServicoService(repository).listarOrdensDeServico());
    }

    @Test void deveAtualizarItensDaOrdemEmDiagnostico() {
        OrdemDeServico ordem = ordem(StatusOS.EM_DIAGNOSTICO);
        Peca peca = new Peca(1, "Filtro", 30, 2);
        Servico servico = new Servico(1, "Troca", 100);

        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(repository.salvar(ordem)).thenReturn(ordem);
        when(pecaRepository.buscarPorId(1)).thenReturn(Optional.of(peca));
        when(servicoRepository.buscarPorId(1)).thenReturn(Optional.of(servico));

        AtualizarItensOrdemDeServicoUseCase.PecaItemInput pecaInput = new AtualizarItensOrdemDeServicoUseCase.PecaItemInput(1, 2);

        OrdemDeServico resultado = new AtualizarItensOrdemDeServicoService(repository, pecaRepository, servicoRepository)
                .atualizarItensOrdemDeServico(1, List.of(pecaInput), List.of(1));

        assertEquals(1, resultado.getPecasNecessarias().size());
        assertEquals(1, resultado.getServicos().size());
        assertEquals(160, resultado.getOrcamento());
    }

    @Test void deveLancarExcecaoQuandoAtualizarItensSemDados() {
        AtualizarItensOrdemDeServicoService service = new AtualizarItensOrdemDeServicoService(repository, pecaRepository, servicoRepository);
        assertThrows(IllegalArgumentException.class, () -> service.atualizarItensOrdemDeServico(1, null, null));
        assertThrows(IllegalArgumentException.class, () -> service.atualizarItensOrdemDeServico(1, List.of(), List.of()));
    }

    @Test void deveLancarExcecaoQuandoOrdemNaoEncontrada() {
        when(repository.buscarPorId(1)).thenReturn(Optional.empty());
        AtualizarItensOrdemDeServicoService service = new AtualizarItensOrdemDeServicoService(repository, pecaRepository, servicoRepository);
        List<AtualizarItensOrdemDeServicoUseCase.PecaItemInput> pecas = List.of(new AtualizarItensOrdemDeServicoUseCase.PecaItemInput(1, 1));
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.atualizarItensOrdemDeServico(1, pecas, null));
    }

    @Test void deveLancarExcecaoQuandoStatusInvalidoParaAtualizarItens() {
        OrdemDeServico ordem = ordem(StatusOS.RECEBIDA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        AtualizarItensOrdemDeServicoService service = new AtualizarItensOrdemDeServicoService(repository, pecaRepository, servicoRepository);
        List<AtualizarItensOrdemDeServicoUseCase.PecaItemInput> pecas = List.of(new AtualizarItensOrdemDeServicoUseCase.PecaItemInput(1, 1));
        assertThrows(IllegalStateException.class, () -> service.atualizarItensOrdemDeServico(1, pecas, null));
    }

    @Test void deveLancarExcecaoQuandoPecaNaoEncontrada() {
        OrdemDeServico ordem = ordem(StatusOS.EM_DIAGNOSTICO);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(pecaRepository.buscarPorId(1)).thenReturn(Optional.empty());

        AtualizarItensOrdemDeServicoService service = new AtualizarItensOrdemDeServicoService(repository, pecaRepository, servicoRepository);
        List<AtualizarItensOrdemDeServicoUseCase.PecaItemInput> pecas = List.of(new AtualizarItensOrdemDeServicoUseCase.PecaItemInput(1, 1));
        assertThrows(EntidadeNaoEncontradaException.class, () -> service.atualizarItensOrdemDeServico(1, pecas, null));
    }

    @Test void deveIniciarDiagnostico() { assertTransicao(StatusOS.RECEBIDA, StatusOS.EM_DIAGNOSTICO, ordem -> new IniciarDiagnosticoService(repository).iniciarDiagnostico(1)); }
    @Test void deveEnviarOrcamento() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "123456789");
        when(clienteRepository.buscarPorId("123")).thenReturn(Optional.of(cliente));
        assertTransicao(StatusOS.EM_DIAGNOSTICO, StatusOS.AGUARDANDO_APROVACAO, ordem ->
            new EnviarOrcamentoService(repository, clienteRepository, notificarCliente).enviarOrcamento(1));
        verify(notificarCliente).notificarOrcamentoAguardandoAprovacao(any(), any());
    }
    @Test void deveAprovarOrcamento() { assertTransicao(StatusOS.AGUARDANDO_APROVACAO, StatusOS.EM_EXECUCAO, ordem -> new AprovarOrcamentoService(repository).aprovarOrcamento(1)); }
    @Test void deveFinalizarReparo() { assertTransicao(StatusOS.EM_EXECUCAO, StatusOS.FINALIZADA, ordem -> new FinalizarReparoService(repository).finalizarReparo(1)); }
    @Test void deveEntregarVeiculo() { assertTransicao(StatusOS.FINALIZADA, StatusOS.ENTREGUE, ordem -> new EntregarVeiculoService(repository).entregarVeiculo(1)); }

    private void assertTransicao(StatusOS origem, StatusOS destino, AcaoTransicao acao) {
        OrdemDeServico ordem = ordem(origem);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        acao.executar(ordem);
        assertEquals(destino, ordem.getStatus());
        verify(repository).salvar(ordem);
    }

    private OrdemDeServico ordem(StatusOS status) {
        LocalDateTime agora = LocalDateTime.now();
        return new OrdemDeServico(1, "123", "ABC1234", List.of(), List.of(), 0, status, agora, agora, "Barulho", null);
    }

    @FunctionalInterface
    private interface AcaoTransicao { void executar(OrdemDeServico ordem); }
}
