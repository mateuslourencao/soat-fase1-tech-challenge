package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {
    private final ClienteRepositoryPort repository = mock(ClienteRepositoryPort.class);

    @Test void deveCadastrarCliente() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "11999999999");
        when(repository.salvar(cliente)).thenReturn(cliente);
        
        Cliente resultado = new CadastrarClienteService(repository).cadastrarCliente(cliente);
        
        assertSame(cliente, resultado);
        verify(repository).salvar(cliente);
    }

    @Test void deveBuscarCliente() {
        Cliente cliente = new Cliente("123", "Joao", "joao@email.com", "11999999999");
        when(repository.buscarPorId("123")).thenReturn(Optional.of(cliente));
        
        Cliente resultado = new BuscarClienteService(repository).buscarCliente("123");
        
        assertSame(cliente, resultado);
    }

    @Test void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(repository.buscarPorId("456")).thenReturn(Optional.empty());
        
        BuscarClienteService service = new BuscarClienteService(repository);
        assertThrows(RuntimeException.class, () -> service.buscarCliente("456"));
    }

    @Test void deveListarClientes() {
        List<Cliente> clientes = List.of(new Cliente("123", "Joao", "joao@email.com", "11999999999"));
        when(repository.listarTodos()).thenReturn(clientes);
        
        List<Cliente> resultado = new ListarClientesService(repository).listarClientes();
        
        assertSame(clientes, resultado);
    }

    @Test void deveAtualizarCliente() {
        Cliente existente = new Cliente("123", "Joao", "joao@email.com", "11999999999");
        Cliente atualizado = new Cliente("123", "Joao Alterado", "joao.novo@email.com", "11888888888");
        
        when(repository.buscarPorId("123")).thenReturn(Optional.of(existente));
        when(repository.salvar(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        Cliente resultado = new AtualizarClienteService(repository).atualizarCliente("123", atualizado);
        
        assertEquals("Joao Alterado", resultado.getNome());
        assertEquals("joao.novo@email.com", resultado.getEmail());
        assertEquals("11888888888", resultado.getTelefone());
        verify(repository).salvar(any(Cliente.class));
    }

    @Test void deveRemoverCliente() {
        new RemoverClienteService(repository).removerCliente("123");
        verify(repository).remover("123");
    }
}
