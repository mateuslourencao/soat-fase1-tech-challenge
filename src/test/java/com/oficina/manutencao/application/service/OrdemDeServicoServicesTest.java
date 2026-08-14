package com.oficina.manutencao.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class OrdemDeServicoServicesTest {
    private final OrdemDeServicoRepositoryPort repository = mock(OrdemDeServicoRepositoryPort.class);

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
        PecasNecessarias peca = new PecasNecessarias(new Peca(1, "Filtro", 30, 2), 2);
        Servico servico = new Servico(1, "Troca", 100);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(repository.salvar(ordem)).thenReturn(ordem);

        OrdemDeServico resultado = new AtualizarItensOrdemDeServicoService(repository)
                .atualizarItensOrdemDeServico(1, List.of(peca), List.of(servico));

        assertEquals(1, resultado.getPecasNecessarias().size());
        assertEquals(1, resultado.getServicos().size());
        assertEquals(160, resultado.getOrcamento());
    }

    @Test void deveIniciarDiagnostico() { assertTransicao(StatusOS.RECEBIDA, StatusOS.EM_DIAGNOSTICO, ordem -> new IniciarDiagnosticoService(repository).IniciarDiagnostico(1)); }
    @Test void deveEnviarOrcamento() { assertTransicao(StatusOS.EM_DIAGNOSTICO, StatusOS.AGUARDANDO_APROVACAO, ordem -> new EnviarOrcamentoService(repository).EnviarOrcamento(1)); }
    @Test void deveAprovarOrcamento() { assertTransicao(StatusOS.AGUARDANDO_APROVACAO, StatusOS.EM_EXECUCAO, ordem -> new AprovarOrcamentoService(repository).AprovarOrcamento(1)); }
    @Test void deveFinalizarReparo() { assertTransicao(StatusOS.EM_EXECUCAO, StatusOS.FINALIZADA, ordem -> new FinalizarReparoService(repository).FinalizarReparo(1)); }
    @Test void deveEntregarVeiculo() { assertTransicao(StatusOS.FINALIZADA, StatusOS.ENTREGUE, ordem -> new EntregarVeiculoService(repository).EntregarVeiculo(1)); }

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
