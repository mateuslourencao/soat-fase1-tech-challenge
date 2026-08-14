package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.CadastrarPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ListarPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ObterPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ReporPecaUseCase;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ObterPecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaResponseDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ReporPecaRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PecaControllerTest {
    private final CadastrarPecaUseCase cadastrarUseCase = mock(CadastrarPecaUseCase.class);
    private final ObterPecaUseCase obterUseCase = mock(ObterPecaUseCase.class);
    private final ListarPecaUseCase listarUseCase = mock(ListarPecaUseCase.class);
    private final ReporPecaUseCase reporUseCase = mock(ReporPecaUseCase.class);
    private final PecaController controller = new PecaController(cadastrarUseCase, obterUseCase, listarUseCase, reporUseCase);

    @Test void deveListarPecas() {
        Peca peca = new Peca(1, "Filtro", 30.0, 10);
        when(listarUseCase.listarPecas()).thenReturn(List.of(peca));
        
        ResponseEntity<List<PecaResponseDTO>> response = controller.listarPecas();
        
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test void deveCadastrarPeca() {
        PecaRequestDTO request = new PecaRequestDTO("Filtro", 30.0, 10);
        Peca peca = new Peca(1, "Filtro", 30.0, 10);
        when(cadastrarUseCase.cadastrarPeca("Filtro", 30.0, 10)).thenReturn(peca);
        
        ResponseEntity<PecaResponseDTO> response = controller.cadastrarPeca(request);
        
        assertEquals(201, response.getStatusCode().value());
        assertEquals(1, response.getBody().id());
    }

    @Test void deveObterPeca() {
        ObterPecaRequestDTO request = new ObterPecaRequestDTO(1, 2);
        
        ResponseEntity<Object> response = controller.ObterPeca(request);
        
        assertEquals(200, response.getStatusCode().value());
        verify(obterUseCase).obterPeca(1, 2);
    }

    @Test void deveReporPeca() {
        ReporPecaRequestDTO request = new ReporPecaRequestDTO(1, 5);
        
        ResponseEntity<Object> response = controller.ReporPeca(request);
        
        assertEquals(200, response.getStatusCode().value());
        verify(reporUseCase).reporEstoque(1, 5);
    }
}
