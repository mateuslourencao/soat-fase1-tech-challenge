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
    private final AtualizarItensOrdemDeServicoUseCase atualizarItens = mock(AtualizarItensOrdemDeServicoUseCase.class);

    private final OrdemDeServicoController controller = new OrdemDeServicoController(
            cadastrarUseCase, listarUseCase, buscarUseCase, atualizarItens
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
}
