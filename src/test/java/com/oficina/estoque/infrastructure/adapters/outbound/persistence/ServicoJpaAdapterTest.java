package com.oficina.estoque.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper.ServicoPersistenceMapper;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServicoJpaAdapterTest {
    private final ServicoJpaRepository repository = mock(ServicoJpaRepository.class);
    private final ServicoPersistenceMapper mapper = mock(ServicoPersistenceMapper.class);
    private final ServicoJpaAdapter adapter = new ServicoJpaAdapter(repository, mapper);

    @Test
    void deveSalvarServico() {
        String desc = "Troca de óleo";
        Double valor = 50.0;
        Servico servico = new Servico(desc, valor);
        ServicoEntity entity = mock(ServicoEntity.class);

        when(mapper.toEntity(any(Servico.class))).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(servico);

        Servico resultado = adapter.salvar(desc, valor);

        assertNotNull(resultado);
        verify(repository).save(entity);
    }

    @Test
    void deveBuscarPorId() {
        int id = 1;
        ServicoEntity entity = mock(ServicoEntity.class);
        Servico servico = new Servico(id, "Troca", 50.0);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(servico);

        Optional<Servico> resultado = adapter.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(servico, resultado.get());
    }

    @Test
    void deveListarServicos() {
        ServicoEntity entity = mock(ServicoEntity.class);
        Servico servico = new Servico(1, "Troca", 50.0);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(servico);

        List<Servico> resultado = adapter.listarServicos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveAtualizarServico() {
        Servico servico = new Servico(1, "Troca", 50.0);
        ServicoEntity entity = mock(ServicoEntity.class);

        when(mapper.toEntity(servico)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(servico);

        Servico resultado = adapter.atualizarServico(servico);

        assertNotNull(resultado);
        verify(repository).save(entity);
    }

    @Test
    void deveDeletarServico() {
        int id = 1;
        doNothing().when(repository).deleteById(id);

        adapter.deletarServico(id);

        verify(repository).deleteById(id);
    }
}
