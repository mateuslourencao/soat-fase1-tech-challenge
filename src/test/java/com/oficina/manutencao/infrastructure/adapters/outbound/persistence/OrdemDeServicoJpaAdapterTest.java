package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.OrdemDeServicoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.OrdemDeServicoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrdemDeServicoJpaAdapterTest {

    @Mock
    private OrdemDeServicoJpaRepository repository;
    @Mock
    private OrdemDeServicoPersistenceMapper mapper;
    @Mock
    private PecaJpaRepository pecaRepository;
    @Mock
    private ServicoJpaRepository servicoRepository;

    private OrdemDeServicoJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new OrdemDeServicoJpaAdapter(repository, mapper, pecaRepository, servicoRepository);
    }

    @Test
    void deveSalvarOrdemDeServico() {
        Peca peca = new Peca(1, "Peca 1", 10.0, 10);
        Servico servico = new Servico(1, "Servico 1", 50.0);
        OrdemDeServico ordem = new OrdemDeServico(1, "123", "ABC1234", "Queixa");
        ordem.registrarAtualizacaoDeItens(List.of(new PecasNecessarias(peca, 1)), List.of(servico));

        OrdemDeServicoEntity entity = mock(OrdemDeServicoEntity.class);
        PecaEntity pecaEntity = mock(PecaEntity.class);
        ServicoEntity servicoEntity = mock(ServicoEntity.class);
        
        when(pecaRepository.getReferenceById(1)).thenReturn(pecaEntity);
        when(servicoRepository.getReferenceById(1)).thenReturn(servicoEntity);
        when(mapper.toEntity(eq(ordem), anyMap(), anyMap())).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(ordem);

        OrdemDeServico resultado = adapter.salvar(ordem);

        assertNotNull(resultado);
        verify(repository).save(entity);
        verify(pecaRepository).getReferenceById(1);
        verify(servicoRepository).getReferenceById(1);
    }

    @Test
    void deveBuscarPorId() {
        OrdemDeServicoEntity entity = mock(OrdemDeServicoEntity.class);
        OrdemDeServico domain = new OrdemDeServico(1, "123", "ABC1234", "Queixa");
        
        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<OrdemDeServico> resultado = adapter.buscarPorId(1);

        assertTrue(resultado.isPresent());
        assertEquals(domain, resultado.get());
    }

    @Test
    void deveListarTodos() {
        OrdemDeServicoEntity entity = mock(OrdemDeServicoEntity.class);
        OrdemDeServico domain = new OrdemDeServico(1, "123", "ABC1234", "Queixa");
        
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        List<OrdemDeServico> resultado = adapter.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveBuscarOrdensDeServicoNoPeriodo() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(1);
        LocalDateTime fim = LocalDateTime.now().plusDays(1);
        
        OrdemDeServicoEntity e1 = mock(OrdemDeServicoEntity.class);
        when(e1.getStatus()).thenReturn(StatusOS.FINALIZADA);
        when(e1.getDataAtualizacao()).thenReturn(LocalDateTime.now());
        
        OrdemDeServicoEntity e2 = mock(OrdemDeServicoEntity.class);
        when(e2.getStatus()).thenReturn(StatusOS.RECEBIDA); // Não deve ser incluída
        
        OrdemDeServico d1 = mock(OrdemDeServico.class);

        when(repository.findAll()).thenReturn(List.of(e1, e2));
        when(mapper.toDomain(e1)).thenReturn(d1);

        List<OrdemDeServico> resultado = adapter.buscarOrdensdeServicoPeriodo(inicio, fim);

        assertEquals(1, resultado.size());
        verify(mapper, times(1)).toDomain(any());
    }
}
