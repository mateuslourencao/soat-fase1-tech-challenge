package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.MetricaExecucao;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.CriarOrdemDeServicoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ItensOSRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OrdemDeServicoControllerTest {

    private final CadastrarOrdemDeServicoUseCase cadastrarUseCase = mock(CadastrarOrdemDeServicoUseCase.class);
    private final ListarOrdensDeServicoUseCase listarUseCase = mock(ListarOrdensDeServicoUseCase.class);
    private final BuscarOrdemDeServicoUseCase buscarUseCase = mock(BuscarOrdemDeServicoUseCase.class);
    private final IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase = mock(IniciarDiagnosticoUseCase.class);
    private final EnviarOrcamentoUseCase enviarOrcamentoUseCase = mock(EnviarOrcamentoUseCase.class);
    private final AprovarOrcamentoUseCase aprovarOrcamentoUseCase = mock(AprovarOrcamentoUseCase.class);
    private final FinalizarReparoUseCase finalizarReparoUseCase = mock(FinalizarReparoUseCase.class);
    private final EntregarVeiculoUseCase entregarVeiculoUseCase = mock(EntregarVeiculoUseCase.class);
    private final AtualizarItensOrdemDeServicoUseCase atualizarItens = mock(AtualizarItensOrdemDeServicoUseCase.class);
    private final CalcularMetricaExecucaoUseCase calcularMetricaExecucao = mock(CalcularMetricaExecucaoUseCase.class);

    private final OrdemDeServicoController controller = new OrdemDeServicoController(
            cadastrarUseCase, listarUseCase, buscarUseCase, atualizarItens,
            iniciarDiagnosticoUseCase, enviarOrcamentoUseCase, aprovarOrcamentoUseCase,
            finalizarReparoUseCase, entregarVeiculoUseCase, calcularMetricaExecucao
    );

    @Test
    void deveCriarOrdemDeServicoComSucesso() {
        CriarOrdemDeServicoRequestDTO request = new CriarOrdemDeServicoRequestDTO("12345678900", "ABC1234", "Troca de óleo");
        OrdemDeServico os = new OrdemDeServico("12345678900", "ABC1234", "Troca de óleo");

        when(cadastrarUseCase.cadastrarOrdemDeServico(any(OrdemDeServico.class))).thenReturn(os);

        ResponseEntity<?> response = controller.criar(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(cadastrarUseCase).cadastrarOrdemDeServico(any(OrdemDeServico.class));
    }

    @Test
    void deveListarOrdensDeServico() {
        OrdemDeServico os = new OrdemDeServico("123", "ABC", "Queixa");
        when(listarUseCase.listarOrdensDeServico()).thenReturn(List.of(os));

        ResponseEntity<?> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(listarUseCase).listarOrdensDeServico();
    }

    @Test
    void deveBuscarOrdemDeServicoPorId() {
        int id = 1;
        OrdemDeServico os = new OrdemDeServico("123", "ABC", "Queixa");
        when(buscarUseCase.buscarOrdemDeServico(id)).thenReturn(os);

        ResponseEntity<?> response = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(buscarUseCase).buscarOrdemDeServico(id);
    }

    @Test
    void deveAtualizarItensDaOrdemDeServico() {
        int id = 1;
        ItensOSRequestDTO request = new ItensOSRequestDTO(List.of(new ItensOSRequestDTO.PecaItemRequest(1, 2)), List.of(2));
        OrdemDeServico os = new OrdemDeServico("123", "ABC", "Queixa");

        when(atualizarItens.atualizarItensOrdemDeServico(eq(id), anyList(), anyList())).thenReturn(os);

        ResponseEntity<?> response = controller.atualizarItens(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(atualizarItens).atualizarItensOrdemDeServico(eq(id), anyList(), anyList());
    }

    @Test
    void deveCalcularMetricaExecucao() {
        int dias = 5;
        MetricaExecucao metricaExecucao = mock(MetricaExecucao.class);
        when(calcularMetricaExecucao.calcularMetricaExecucao(dias)).thenReturn(metricaExecucao);

        ResponseEntity<?> response = controller.calcularMetricaExecucao(dias);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deveIniciarDiagnostico() {
        int id = 1;
        ResponseEntity<Void> response = controller.iniciarDiagnostico(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(iniciarDiagnosticoUseCase).iniciarDiagnostico(id);
    }

    @Test
    void deveEnviarOrcamento() {
        int id = 1;
        OrdemDeServico os = new OrdemDeServico("123", "ABC", "Queixa");
        when(buscarUseCase.buscarOrdemDeServico(id)).thenReturn(os);
        
        ResponseEntity<?> response = controller.enviarOrcamento(id);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(enviarOrcamentoUseCase).enviarOrcamento(id);
        verify(buscarUseCase).buscarOrdemDeServico(id);
    }

    @Test
    void deveAprovarOrcamento() {
        int id = 1;
        ResponseEntity<Void> response = controller.aprovarOrcamento(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(aprovarOrcamentoUseCase).aprovarOrcamento(id);
    }

    @Test
    void deveFinalizarReparo() {
        int id = 1;
        ResponseEntity<Void> response = controller.finalizarReparo(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(finalizarReparoUseCase).finalizarReparo(id);
    }

    @Test
    void deveEntregarVeiculo() {
        int id = 1;
        ResponseEntity<Void> response = controller.entregarVeiculo(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(entregarVeiculoUseCase).entregarVeiculo(id);
    }
}
