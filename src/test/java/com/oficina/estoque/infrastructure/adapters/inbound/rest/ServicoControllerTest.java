package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicoControllerTest {

    private final ListarServicoUseCase listarUseCase = mock(ListarServicoUseCase.class);
    private final CadastrarServicoUseCase cadastrarUseCase = mock(CadastrarServicoUseCase.class);
    private final AtualizarServicoUseCase atualizarUseCase = mock(AtualizarServicoUseCase.class);
    private final RemoverServicoUseCase removerUseCase = mock(RemoverServicoUseCase.class);
    private final BuscarServicoUseCase buscarUseCase = mock(BuscarServicoUseCase.class);

    private final ServicoController controller = new ServicoController(
            listarUseCase, cadastrarUseCase, atualizarUseCase, removerUseCase, buscarUseCase
    );

    @Test
    void deveListarServicos() {
        Servico servico = new Servico(1, "Troca de óleo", 50.0);
        when(listarUseCase.listarServico()).thenReturn(List.of(servico));

        ResponseEntity<?> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listarUseCase).listarServico();
    }

    @Test
    void deveCriarServico() {
        ServicoRequestDTO request = new ServicoRequestDTO("Troca de óleo", 50.0);
        Servico servico = new Servico(1, "Troca de óleo", 50.0);
        when(cadastrarUseCase.cadastrarServico("Troca de óleo", 50.0)).thenReturn(servico);

        ResponseEntity<?> response = controller.criar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cadastrarUseCase).cadastrarServico("Troca de óleo", 50.0);
    }

    @Test
    void deveBuscarServicoPorId() {
        int id = 1;
        Servico servico = new Servico(id, "Troca de óleo", 50.0);
        when(buscarUseCase.buscarServico(id)).thenReturn(servico);

        ResponseEntity<?> response = controller.buscarPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(buscarUseCase).buscarServico(id);
    }

    @Test
    void deveRemoverServico() {
        int id = 1;
        doNothing().when(removerUseCase).removerServico(id);

        ResponseEntity<?> response = controller.remover(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(removerUseCase).removerServico(id);
    }

    @Test
    void deveAtualizarServico() {
        int id = 1;
        ServicoRequestDTO request = new ServicoRequestDTO("Troca de óleo premium", 70.0);
        Servico servicoAtualizado = new Servico(id, "Troca de óleo premium", 70.0);
        when(atualizarUseCase.atualizarServico(any(Servico.class))).thenReturn(servicoAtualizado);

        ResponseEntity<?> response = controller.atualizar(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizarUseCase).atualizarServico(any(Servico.class));
    }
}
