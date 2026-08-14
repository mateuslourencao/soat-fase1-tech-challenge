package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class ClienteServicesTest {
    private final ClienteRepositoryPort repository = mock(ClienteRepositoryPort.class);
    private final Cliente cliente = new Cliente("123", "Ana", "ana@oficina.com", "11999999999");

    @Test void deveCadastrarCliente() {
        when(repository.salvar(cliente)).thenReturn(cliente);
        assertSame(cliente, new CadastrarClienteService(repository).cadastrarCliente(cliente));
    }

    @Test void deveBuscarCliente() {
        when(repository.buscarPorId("123")).thenReturn(Optional.of(cliente));
        assertSame(cliente, new BuscarClienteService(repository).buscarCliente("123"));
    }

    @Test void deveListarClientes() {
        List<Cliente> clientes = List.of(cliente);
        when(repository.listarTodos()).thenReturn(clientes);
        assertSame(clientes, new ListarClientesService(repository).listarClientes());
    }

    @Test void deveAtualizarClienteMantendoDocumentoExistente() {
        Cliente atualizado = new Cliente("outro", "Ana Silva", "ana.silva@oficina.com", "11888888888");
        when(repository.buscarPorId("123")).thenReturn(Optional.of(cliente));
        when(repository.salvar(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = new AtualizarClienteService(repository).atualizarCliente("123", atualizado);

        assertEquals("123", resultado.getDocumento());
        assertEquals("Ana Silva", resultado.getNome());
    }

    @Test void deveRemoverCliente() {
        when(repository.buscarPorId("123")).thenReturn(Optional.of(cliente));
        new RemoverClienteService(repository).removerCliente("123");
        verify(repository).remover("123");
    }
}
