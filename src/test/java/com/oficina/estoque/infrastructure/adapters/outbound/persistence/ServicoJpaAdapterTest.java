package com.oficina.estoque.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper.ServicoPersistenceMapper;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ServicoJpaAdapterTest {
    private final ServicoJpaRepository repository = mock(ServicoJpaRepository.class);
    private final ServicoPersistenceMapper mapper = mock(ServicoPersistenceMapper.class);
    private final ServicoJpaAdapter adapter = new ServicoJpaAdapter(repository, mapper);

    @Test void deveSalvarServico() {
        Servico servico = new Servico("Troca", 100.0);
        ServicoEntity entity = new ServicoEntity(1, "Troca", BigDecimal.valueOf(100.0));
        
        when(mapper.toEntity(any(Servico.class))).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(new Servico(1, "Troca", 100.0));
        
        Servico resultado = adapter.salvar("Troca", 100.0);
        
        assertEquals("Troca", resultado.getDescricao());
        assertEquals(1, resultado.getId());
        verify(repository).save(entity);
    }

    @Test void deveBuscarPorId() {
        Servico servico = new Servico(1, "Troca", 100.0);
        ServicoEntity entity = new ServicoEntity(1, "Troca", BigDecimal.valueOf(100.0));
        
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(servico);
        
        Optional<Servico> resultado = adapter.buscarPorId(1);
        
        assertTrue(resultado.isPresent());
        assertEquals(servico, resultado.get());
    }

    @Test void deveListarServicos() {
        Servico servico = new Servico(1, "Troca", 100.0);
        ServicoEntity entity = new ServicoEntity(1, "Troca", BigDecimal.valueOf(100.0));
        
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(servico);
        
        List<Servico> resultado = adapter.listarServicos();
        
        assertEquals(1, resultado.size());
        assertEquals(servico, resultado.get(0));
    }

    @Test void deveAtualizarServico() {
        Servico servico = new Servico(1, "Troca", 120.0);
        ServicoEntity entity = new ServicoEntity(1, "Troca", BigDecimal.valueOf(120.0));
        
        when(mapper.toEntity(servico)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(servico);
        
        Servico resultado = adapter.atualizarServico(servico);
        
        assertEquals(servico, resultado);
        verify(repository).save(entity);
    }

    @Test void deveDeletarServico() {
        adapter.deletarServico(1);
        verify(repository).deleteById(1);
    }
}
