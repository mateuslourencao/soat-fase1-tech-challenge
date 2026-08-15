package com.oficina.administrativo.infrastructure.adapters.outbound.persistence;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.entity.FuncionarioEntity;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.mapper.FuncionarioPersistenceMapper;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.repository.FuncionarioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuncionarioJpaAdapterTest {

    @Mock
    private FuncionarioJpaRepository repository;
    @Mock
    private FuncionarioPersistenceMapper mapper;

    private FuncionarioJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new FuncionarioJpaAdapter(repository, mapper);
    }

    @Test
    void deveBuscarPorEmail() {
        String email = "test@test.com";
        FuncionarioEntity entity = mock(FuncionarioEntity.class);
        Funcionario domain = new Funcionario(1, "Nome", email, "hash", PerfilFuncionario.ADMIN, true);

        when(repository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Funcionario> resultado = adapter.buscarPorEmail(email);

        assertTrue(resultado.isPresent());
        assertEquals(domain, resultado.get());
    }

    @Test
    void deveBuscarPorId() {
        int id = 1;
        FuncionarioEntity entity = mock(FuncionarioEntity.class);
        Funcionario domain = new Funcionario(id, "Nome", "email", "hash", PerfilFuncionario.ADMIN, true);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<Funcionario> resultado = adapter.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(domain, resultado.get());
    }

    @Test
    void deveListarTodos() {
        FuncionarioEntity entity = mock(FuncionarioEntity.class);
        Funcionario domain = new Funcionario(1, "Nome", "email", "hash", PerfilFuncionario.ADMIN, true);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<Funcionario> resultado = adapter.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveSalvarFuncionario() {
        Funcionario domain = new Funcionario(1, "Nome", "email", "hash", PerfilFuncionario.ADMIN, true);
        FuncionarioEntity entity = mock(FuncionarioEntity.class);

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(domain);

        Funcionario resultado = adapter.salvar(domain);

        assertNotNull(resultado);
        assertEquals(domain, resultado);
        verify(repository).save(entity);
    }
}
