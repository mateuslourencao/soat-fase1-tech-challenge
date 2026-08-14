package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    private final CadastrarClienteUseCase cadastrarUseCase = mock(CadastrarClienteUseCase.class);
    private final AtualizarClienteUseCase atualizarUseCase = mock(AtualizarClienteUseCase.class);
    private final BuscarClienteUseCase buscarUseCase = mock(BuscarClienteUseCase.class);
    private final ListarClientesUseCase listarUseCase = mock(ListarClientesUseCase.class);
    private final RemoverClienteUseCase removerUseCase = mock(RemoverClienteUseCase.class);

    private final ClienteController controller = new ClienteController(
            cadastrarUseCase, atualizarUseCase, buscarUseCase, listarUseCase, removerUseCase
    );

    @Test
    void deveCriarClienteComSucesso() {
        ClienteRequestDTO request = new ClienteRequestDTO("123", "João", "joao@email.com", "1234");
        Cliente cliente = new Cliente("123", "João", "joao@email.com", "1234");
        when(cadastrarUseCase.cadastrarCliente(any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<?> response = controller.criar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(cadastrarUseCase).cadastrarCliente(any(Cliente.class));
    }

    @Test
    void deveListarClientes() {
        Cliente cliente = new Cliente("123", "João", "joao@email.com", "1234");
        when(listarUseCase.listarClientes()).thenReturn(List.of(cliente));

        ResponseEntity<?> response = controller.listar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(listarUseCase).listarClientes();
    }

    @Test
    void deveBuscarClientePorDocumento() {
        String doc = "123";
        Cliente cliente = new Cliente(doc, "João", "joao@email.com", "1234");
        when(buscarUseCase.buscarCliente(doc)).thenReturn(cliente);

        ResponseEntity<?> response = controller.buscarPorId(doc);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(buscarUseCase).buscarCliente(doc);
    }

    @Test
    void deveAtualizarCliente() {
        String doc = "123";
        ClienteRequestDTO request = new ClienteRequestDTO(doc, "João Silva", "joao@email.com", "1234");
        Cliente cliente = new Cliente(doc, "João Silva", "joao@email.com", "1234");
        when(atualizarUseCase.atualizarCliente(eq(doc), any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<?> response = controller.atualizar(doc, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(atualizarUseCase).atualizarCliente(eq(doc), any(Cliente.class));
    }

    @Test
    void deveRemoverCliente() {
        String doc = "123";
        doNothing().when(removerUseCase).removerCliente(doc);

        ResponseEntity<?> response = controller.remover(doc);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(removerUseCase).removerCliente(doc);
    }
}
