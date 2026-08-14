package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.OrdemDeServicoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.OrdemDeServicoJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OrdemDeServicoJpaAdapterTest {
    private final OrdemDeServicoJpaRepository repository = mock(OrdemDeServicoJpaRepository.class);
    private final OrdemDeServicoPersistenceMapper mapper = mock(OrdemDeServicoPersistenceMapper.class);
    private final PecaJpaRepository pecaRepository = mock(PecaJpaRepository.class);
    private final ServicoJpaRepository servicoRepository = mock(ServicoJpaRepository.class);
    private final OrdemDeServicoJpaAdapter adapter = new OrdemDeServicoJpaAdapter(repository, mapper, pecaRepository, servicoRepository);

    @Test void deveSalvarOrdemDeServico() {
        OrdemDeServico ordem = new OrdemDeServico("123", "ABC1234", "Queixa");
        OrdemDeServicoEntity entity = new OrdemDeServicoEntity();
        
        when(mapper.toEntity(eq(ordem), anyMap(), anyMap())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(ordem);

        OrdemDeServico resultado = adapter.salvar(ordem);

        assertEquals(ordem, resultado);
        verify(repository).save(entity);
    }

    @Test void deveBuscarPorId() {
        OrdemDeServicoEntity entity = new OrdemDeServicoEntity();
        OrdemDeServico domain = new OrdemDeServico("123", "ABC1234", "Queixa");
        
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<OrdemDeServico> resultado = adapter.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(domain, resultado.get());
    }

    @Test void deveListarTodos() {
        OrdemDeServicoEntity entity = new OrdemDeServicoEntity();
        OrdemDeServico domain = new OrdemDeServico("123", "ABC1234", "Queixa");
        
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<OrdemDeServico> resultado = adapter.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals(domain, resultado.get(0));
    }
}
