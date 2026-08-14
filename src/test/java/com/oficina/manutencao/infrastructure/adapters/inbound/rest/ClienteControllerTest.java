package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClienteControllerTest {
    private final CadastrarClienteUseCase cadastrarUseCase = mock(CadastrarClienteUseCase.class);
    private final AtualizarClienteUseCase atualizarUseCase = mock(AtualizarClienteUseCase.class);
    private final BuscarClienteUseCase buscarUseCase = mock(BuscarClienteUseCase.class);
    private final ListarClientesUseCase listarUseCase = mock(ListarClientesUseCase.class);
    private final RemoverClienteUseCase removerUseCase = mock(RemoverClienteUseCase.class);
    private final ClienteController controller = new ClienteController(cadastrarUseCase, atualizarUseCase, buscarUseCase, listarUseCase, removerUseCase);

    @Test void deveCriarCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO("123", "Joao", "joao@email.com", "11999999999");
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "11999999999");
        when(cadastrarUseCase.cadastrarCliente(any())).thenReturn(cliente);

        ResponseEntity<ClienteResponseDTO> response = controller.criar(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("123", response.getBody().documento());
    }

    @Test void deveListarClientes() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "11999999999");
        when(listarUseCase.listarClientes()).thenReturn(List.of(cliente));

        ResponseEntity<List<ClienteResponseDTO>> response = controller.listar();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test void deveBuscarPorDocumento() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "11999999999");
        when(buscarUseCase.buscarCliente("123")).thenReturn(cliente);

        ResponseEntity<ClienteResponseDTO> response = controller.buscarPorId("123");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Joao", response.getBody().nome());
    }

    @Test void deveAtualizarCliente() {
        ClienteRequestDTO request = new ClienteRequestDTO("123", "Joao Alt", "joao@email.com", "11999999999");
        Cliente cliente = new Cliente("123", "Joao Alt", "joao@email.com", "11999999999");
        when(atualizarUseCase.atualizarCliente(eq("123"), any())).thenReturn(cliente);

        ResponseEntity<ClienteResponseDTO> response = controller.atualizar("123", request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Joao Alt", response.getBody().nome());
    }

    @Test void deveRemoverCliente() {
        ResponseEntity<Void> response = controller.remover("123");
        assertEquals(204, response.getStatusCode().value());
        verify(removerUseCase).removerCliente("123");
    }
}
