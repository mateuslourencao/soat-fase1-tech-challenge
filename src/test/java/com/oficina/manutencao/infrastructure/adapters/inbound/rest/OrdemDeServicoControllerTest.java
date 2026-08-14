package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.CadastrarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.CriarOrdemDeServicoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ItensOSRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.OrdemDeServicoResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrdemDeServicoControllerTest {
    private final CadastrarOrdemDeServicoUseCase cadastrarUseCase = mock(CadastrarOrdemDeServicoUseCase.class);
    private final ListarOrdensDeServicoUseCase listarUseCase = mock(ListarOrdensDeServicoUseCase.class);
    private final BuscarOrdemDeServicoUseCase buscarUseCase = mock(BuscarOrdemDeServicoUseCase.class);
    private final AtualizarItensOrdemDeServicoUseCase atualizarUseCase = mock(AtualizarItensOrdemDeServicoUseCase.class);
    private final OrdemDeServicoController controller = new OrdemDeServicoController(cadastrarUseCase, listarUseCase, buscarUseCase, atualizarUseCase);

    @Test void deveCriarOrdemDeServico() {
        CriarOrdemDeServicoRequestDTO request = new CriarOrdemDeServicoRequestDTO("123", "ABC1234", "Queixa");
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Queixa");
        
        when(cadastrarUseCase.cadastrarOrdemDeServico(any(OrdemDeServico.class))).thenReturn(os);
        
        ResponseEntity<OrdemDeServicoResponseDTO> response = controller.criar(request);
        
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().documentoCliente());
    }

    @Test void deveListarOrdensDeServico() {
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Queixa");
        when(listarUseCase.listarOrdensDeServico()).thenReturn(List.of(os));
        
        ResponseEntity<List<OrdemDeServicoResponseDTO>> response = controller.listar();
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test void deveBuscarPorId() {
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Queixa");
        when(buscarUseCase.buscarOrdemDeServico(1)).thenReturn(os);
        
        ResponseEntity<OrdemDeServicoResponseDTO> response = controller.buscarPorId(1);
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals("123", response.getBody().documentoCliente());
    }

    @Test void deveAtualizarItens() {
        ItensOSRequestDTO request = new ItensOSRequestDTO(List.of(), List.of());
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Queixa");
        
        when(atualizarUseCase.atualizarItensOrdemDeServico(eq(1), anyList(), anyList())).thenReturn(os);
        
        ResponseEntity<OrdemDeServicoResponseDTO> response = controller.atualizarItens(1, request);
        
        assertEquals(200, response.getStatusCode().value());
        verify(atualizarUseCase).atualizarItensOrdemDeServico(eq(1), anyList(), anyList());
    }
}
