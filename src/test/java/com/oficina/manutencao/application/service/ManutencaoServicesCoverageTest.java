package com.oficina.manutencao.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase.PecaItemInput;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManutencaoServicesCoverageTest {
    @Test
    void deveExecutarTransicoesValidasERejeitarEstadoIncorretoOuOsAusente() {
        OrdemDeServicoRepositoryPort repository = mock(OrdemDeServicoRepositoryPort.class);
        OrdemDeServico ordem = ordemComStatus(StatusOS.RECEBIDA);
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        new IniciarDiagnosticoService(repository).iniciarDiagnostico(1);
        assertEquals(StatusOS.EM_DIAGNOSTICO, ordem.getStatus());

        new EnviarOrcamentoService(repository).enviarOrcamento(1);
        new AprovarOrcamentoService(repository).aprovarOrcamento(1);
        new FinalizarReparoService(repository).finalizarReparo(1);
        new EntregarVeiculoService(repository).entregarVeiculo(1);
        assertEquals(StatusOS.ENTREGUE, ordem.getStatus());
        verify(repository, times(5)).salvar(ordem);

        assertThrows(IllegalStateException.class, () -> new IniciarDiagnosticoService(repository).iniciarDiagnostico(1));
        when(repository.buscarPorId(2)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> new IniciarDiagnosticoService(repository).iniciarDiagnostico(2));
    }

    @Test
    void deveAtualizarItensNoDiagnosticoEValidarPreCondicoes() {
        OrdemDeServicoRepositoryPort ordemRepository = mock(OrdemDeServicoRepositoryPort.class);
        PecaRepositoryPort pecaRepository = mock(PecaRepositoryPort.class);
        ServicoRepositoryPort servicoRepository = mock(ServicoRepositoryPort.class);
        AtualizarItensOrdemDeServicoService service = new AtualizarItensOrdemDeServicoService(ordemRepository, pecaRepository, servicoRepository);
        OrdemDeServico ordem = ordemComStatus(StatusOS.EM_DIAGNOSTICO);
        Peca peca = new Peca(4, "Pastilha", 50, 2);
        Servico servico = new Servico(8, "Troca", 100);
        when(ordemRepository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(pecaRepository.buscarPorId(4)).thenReturn(Optional.of(peca));
        when(servicoRepository.buscarPorId(8)).thenReturn(Optional.of(servico));
        when(ordemRepository.salvar(ordem)).thenReturn(ordem);

        assertSame(ordem, service.atualizarItensOrdemDeServico(1, List.of(new PecaItemInput(4, 2)), List.of(8)));
        assertEquals(200, ordem.getOrcamento());
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.atualizarItensOrdemDeServico(1, List.of(), List.of())),
                () -> assertThrows(IllegalArgumentException.class, () -> service.atualizarItensOrdemDeServico(2, List.of(new PecaItemInput(4, 1)), List.of())));

        OrdemDeServico recebida = ordemComStatus(StatusOS.RECEBIDA);
        when(ordemRepository.buscarPorId(3)).thenReturn(Optional.of(recebida));
        assertThrows(IllegalStateException.class, () -> service.atualizarItensOrdemDeServico(3, List.of(new PecaItemInput(4, 1)), List.of()));
        when(pecaRepository.buscarPorId(5)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.atualizarItensOrdemDeServico(1, List.of(new PecaItemInput(5, 1)), List.of()));
        when(servicoRepository.buscarPorId(9)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.atualizarItensOrdemDeServico(1, List.of(), List.of(9)));
    }

    @Test
    void deveExecutarServicosSimplesDeClienteVeiculoEOrdem() {
        ClienteRepositoryPort clientes = mock(ClienteRepositoryPort.class);
        VeiculoRepositoryPort veiculos = mock(VeiculoRepositoryPort.class);
        OrdemDeServicoRepositoryPort ordens = mock(OrdemDeServicoRepositoryPort.class);
        Cliente cliente = new Cliente("1", "Ana", "a@a.com", "11");
        Veiculo veiculo = new Veiculo("ABC", "Ford", "Ka", 2020);
        OrdemDeServico ordem = new OrdemDeServico(1, "1", "ABC", "Ruido");
        when(clientes.buscarPorId("1")).thenReturn(Optional.of(cliente));
        when(veiculos.buscarPorId("ABC")).thenReturn(Optional.of(veiculo));
        when(ordens.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(clientes.listarTodos()).thenReturn(List.of(cliente));
        when(veiculos.listarTodos()).thenReturn(List.of(veiculo));
        when(ordens.listarTodos()).thenReturn(List.of(ordem));
        when(clientes.salvar(any())).thenAnswer(i -> i.getArgument(0));
        when(veiculos.salvar(any())).thenAnswer(i -> i.getArgument(0));
        when(ordens.salvar(any())).thenAnswer(i -> i.getArgument(0));

        assertSame(cliente, new BuscarClienteService(clientes).buscarCliente("1"));
        assertSame(veiculo, new BuscarVeiculoService(veiculos).buscarVeiculo("ABC"));
        assertSame(ordem, new BuscarOrdemDeServicoService(ordens).buscarOrdemDeServico(1));
        assertEquals(List.of(cliente), new ListarClientesService(clientes).listarClientes());
        assertEquals(List.of(veiculo), new ListarVeiculosService(veiculos).listarVeiculos());
        assertEquals(List.of(ordem), new ListarOrdensDeServicoService(ordens).listarOrdensDeServico());
        assertEquals("Novo", new AtualizarClienteService(clientes).atualizarCliente("1", new Cliente("x", "Novo", "n@a", "22")).getNome());
        assertEquals("Novo", new AtualizarVeiculoService(veiculos).atualizarVeiculo("ABC", new Veiculo("x", "Novo", "M", 2024)).getMarca());
        assertSame(cliente, new CadastrarClienteService(clientes).cadastrarCliente(cliente));
        assertSame(veiculo, new CadastrarVeiculoService(veiculos).cadastrarVeiculo(veiculo));
        assertSame(ordem, new CadastrarOrdemDeServicoService(ordens).cadastrarOrdemDeServico(ordem));
        assertThrows(IllegalArgumentException.class, () -> new CadastrarOrdemDeServicoService(ordens).cadastrarOrdemDeServico(null));
        new RemoverClienteService(clientes).removerCliente("1");
        new RemoverVeiculoService(veiculos).removerVeiculo("ABC");
        verify(clientes).remover("1");
        verify(veiculos).remover("ABC");
    }

    private OrdemDeServico ordemComStatus(StatusOS status) {
        OrdemDeServico ordem = new OrdemDeServico(1, "1", "ABC", "Ruido");
        ordem.alterarStatus(status);
        return ordem;
    }
}
