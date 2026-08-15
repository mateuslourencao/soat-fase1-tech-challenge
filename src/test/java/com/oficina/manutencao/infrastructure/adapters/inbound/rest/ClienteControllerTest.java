package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ClienteResponseDTO;
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
    void deveCriarClienteLimpandoDocumento() {
        ClienteRequestDTO request = new ClienteRequestDTO("123.456.789-00", "João", "joao@email.com", "1234");
        Cliente cliente = new Cliente("12345678900", "João", "joao@email.com", "1234");
        when(cadastrarUseCase.cadastrarCliente(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<ClienteResponseDTO> response = controller.criar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("12345678900", response.getBody().documento());
        verify(cadastrarUseCase).cadastrarCliente(argThat(c -> c.getDocumento().equals("12345678900")));
    }

    @Test
    void deveBuscarClientePorDocumentoFormatado() {
        String docFormatado = "123.456.789-00";
        String docLimpo = "12345678900";
        Cliente cliente = new Cliente(docLimpo, "João", "joao@email.com", "1234");
        when(buscarUseCase.buscarCliente(docLimpo)).thenReturn(cliente);

        ResponseEntity<ClienteResponseDTO> response = controller.buscarPorId(docFormatado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(docLimpo, response.getBody().documento());
        verify(buscarUseCase).buscarCliente(docLimpo);
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
