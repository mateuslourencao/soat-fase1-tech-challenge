package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.ClienteEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.ClientePersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.ClienteJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteJpaAdapterTest {
    private final ClienteJpaRepository repository = mock(ClienteJpaRepository.class);
    private final ClientePersistenceMapper mapper = mock(ClientePersistenceMapper.class);
    private final ClienteJpaAdapter adapter = new ClienteJpaAdapter(repository, mapper);

    @Test
    void deveSalvarClienteComSucesso() {
        Cliente cliente = new Cliente("123", "Nome", "email@test.com", "1234");
        ClienteEntity entity = new ClienteEntity("123", "Nome", "email@test.com", "1234");
        
        when(mapper.toEntity(cliente)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(cliente);

        Cliente resultado = adapter.salvar(cliente);

        assertNotNull(resultado);
        assertEquals(cliente.getDocumento(), resultado.getDocumento());
        verify(repository).save(entity);
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        String documento = "123";
        ClienteEntity entity = new ClienteEntity(documento, "Nome", "email@test.com", "1234");

        when(repository.findById(documento)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new Cliente(documento, "Nome", "email@test.com", "1234"));

        Optional<Cliente> resultado = adapter.buscarPorId(documento);

        assertTrue(resultado.isPresent());
        assertEquals(documento, resultado.get().getDocumento());
    }

    @Test
    void deveListarTodosComSucesso() {
        ClienteEntity entity = new ClienteEntity("123", "Nome", "email@test.com", "1234");

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new Cliente("123", "Nome", "email@test.com", "1234"));

        List<Cliente> resultado = adapter.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveRemoverClienteComSucesso() {
        String documento = "123";

        adapter.remover(documento);

        verify(repository).deleteById(documento);
    }
}
