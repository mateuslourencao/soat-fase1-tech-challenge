package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ObterPecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ReporPecaRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PecaControllerTest {

    private final CadastrarPecaUseCase cadastrarUseCase = mock(CadastrarPecaUseCase.class);
    private final ObterPecaUseCase obterUseCase = mock(ObterPecaUseCase.class);
    private final ListarPecaUseCase listarUseCase = mock(ListarPecaUseCase.class);
    private final ReporPecaUseCase reporUseCase = mock(ReporPecaUseCase.class);
    private final AtualizarPecaUseCase atualizarUseCase = mock(AtualizarPecaUseCase.class);
    private final RemoverPecaUseCase removerUseCase = mock(RemoverPecaUseCase.class);

    private final PecaController controller = new PecaController(
            cadastrarUseCase, obterUseCase, listarUseCase, reporUseCase, atualizarUseCase, removerUseCase
    );

    @Test
    void deveListarPecas() {
        Peca peca = new Peca(1, "Pastilha", 100.0, 10);
        when(listarUseCase.listarPecas()).thenReturn(List.of(peca));

        ResponseEntity<?> response = controller.listarPecas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listarUseCase).listarPecas();
    }

    @Test
    void deveCadastrarPeca() {
        PecaRequestDTO request = new PecaRequestDTO("Pastilha", 100.0, 10);
        Peca peca = new Peca(1, "Pastilha", 100.0, 10);
        when(cadastrarUseCase.cadastrarPeca("Pastilha", 100.0, 10)).thenReturn(peca);

        ResponseEntity<?> response = controller.cadastrarPeca(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cadastrarUseCase).cadastrarPeca("Pastilha", 100.0, 10);
    }

    @Test
    void deveObterPecaDoEstoque() {
        Peca peca = new Peca(1, "Pastilha", 100.0, 10);
        ObterPecaRequestDTO request = new ObterPecaRequestDTO(1, 2);
        when(obterUseCase.obterPeca(1, 2)).thenReturn(peca);

        ResponseEntity<?> response = controller.ObterPeca(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(obterUseCase).obterPeca(1, 2);
    }

    @Test
    void deveReporPecaNoEstoque() {
        Peca peca = new Peca(1, "Pastilha", 100.0, 10);
        ReporPecaRequestDTO request = new ReporPecaRequestDTO(1, 5);
        when(reporUseCase.reporEstoque(1, 5)).thenReturn(peca);

        ResponseEntity<?> response = controller.ReporPeca(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reporUseCase).reporEstoque(1, 5);
    }

    @Test
    void deveAtualizarPeca() {
        int id = 1;
        PecaRequestDTO request = new PecaRequestDTO("Pastilha Atualizada", 120.0, 15);
        Peca peca = new Peca(id, "Pastilha Atualizada", 120.0, 15);
        when(atualizarUseCase.atualizarPeca(any(Peca.class))).thenReturn(peca);

        ResponseEntity<?> response = controller.atualizarPeca(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizarUseCase).atualizarPeca(any(Peca.class));
    }

    @Test
    void deveRemoverPeca() {
        int id = 1;
        ResponseEntity<Void> response = controller.removerPeca(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(removerUseCase).removerPeca(id);
    }
}
