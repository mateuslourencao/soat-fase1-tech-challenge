package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ServicoControllerTest {
    private final ListarServicoUseCase listarUseCase = mock(ListarServicoUseCase.class);
    private final CadastrarServicoUseCase cadastrarUseCase = mock(CadastrarServicoUseCase.class);
    private final AtualizarServicoUseCase atualizarUseCase = mock(AtualizarServicoUseCase.class);
    private final RemoverServicoUseCase removerUseCase = mock(RemoverServicoUseCase.class);
    private final BuscarServicoUseCase buscarUseCase = mock(BuscarServicoUseCase.class);
    private final ServicoController controller = new ServicoController(listarUseCase, cadastrarUseCase, atualizarUseCase, removerUseCase, buscarUseCase);

    @Test void deveListarServicos() {
        Servico servico = new Servico(1, "Troca", 100.0);
        when(listarUseCase.listarServico()).thenReturn(List.of(servico));
        
        ResponseEntity<List<ServicoResponseDTO>> response = controller.listar();
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test void deveCriarServico() {
        ServicoRequestDTO request = new ServicoRequestDTO("Troca", 100.0);
        Servico servico = new Servico(1, "Troca", 100.0);
        when(cadastrarUseCase.cadastrarServico("Troca", 100.0)).thenReturn(servico);
        
        ResponseEntity<ServicoResponseDTO> response = controller.criar(request);
        
        assertEquals(201, response.getStatusCode().value());
        assertEquals(1, response.getBody().id());
    }

    @Test void deveBuscarPorId() {
        Servico servico = new Servico(1, "Troca", 100.0);
        when(buscarUseCase.buscarServico(1)).thenReturn(servico);
        
        ResponseEntity<ServicoResponseDTO> response = controller.buscarPorId(1);
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Troca", response.getBody().descricao());
    }

    @Test void deveRemoverServico() {
        ResponseEntity<Void> response = controller.remover(1);
        assertEquals(200, response.getStatusCode().value());
        verify(removerUseCase).removerServico(1);
    }

    @Test void deveAtualizarServico() {
        ServicoRequestDTO request = new ServicoRequestDTO("Troca Alt", 120.0);
        
        ResponseEntity<ServicoResponseDTO> response = controller.atualizar(1, request);
        
        assertEquals(200, response.getStatusCode().value());
        verify(atualizarUseCase).atualizarServico(any(Servico.class));
    }
}
