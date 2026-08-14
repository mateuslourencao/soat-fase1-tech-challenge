package com.oficina.estoque.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper.PecaPersistenceMapper;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PecaJpaAdapterTest {
    private final PecaJpaRepository repository = mock(PecaJpaRepository.class);
    private final PecaPersistenceMapper mapper = mock(PecaPersistenceMapper.class);
    private final PecaJpaAdapter adapter = new PecaJpaAdapter(repository, mapper);

    @Test
    void deveSalvarPeca() {
        Peca peca = new Peca(1, "Pastilha", 100.0, 10);
        PecaEntity entity = mock(PecaEntity.class);
        
        when(mapper.toEntity(peca)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(peca);

        Peca resultado = adapter.salvar(peca);

        assertNotNull(resultado);
        verify(repository).save(entity);
    }

    @Test
    void deveBuscarPorId() {
        int id = 1;
        PecaEntity entity = mock(PecaEntity.class);
        Peca peca = new Peca(id, "Pastilha", 100.0, 10);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(peca);

        Optional<Peca> resultado = adapter.buscarPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(peca, resultado.get());
    }

    @Test
    void deveListarPecas() {
        PecaEntity entity = mock(PecaEntity.class);
        Peca peca = new Peca(1, "Pastilha", 100.0, 10);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(peca);

        List<Peca> resultado = adapter.listarPecas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }
}
