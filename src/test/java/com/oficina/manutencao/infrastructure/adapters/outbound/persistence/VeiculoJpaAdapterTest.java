package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.VeiculoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.VeiculoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.VeiculoJpaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VeiculoJpaAdapterTest {
    private final VeiculoJpaRepository repository = mock(VeiculoJpaRepository.class);
    private final VeiculoPersistenceMapper mapper = mock(VeiculoPersistenceMapper.class);
    private final VeiculoJpaAdapter adapter = new VeiculoJpaAdapter(repository, mapper);

    @Test
    void deveSalvarVeiculoComSucesso() {
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Fiesta", 2020);
        VeiculoEntity entity = new VeiculoEntity("ABC1234", "Ford", "Fiesta", 2020);
        
        when(mapper.toEntity(veiculo)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(veiculo);

        Veiculo resultado = adapter.salvar(veiculo);

        assertNotNull(resultado);
        assertEquals(veiculo.getPlaca(), resultado.getPlaca());
        verify(repository).save(entity);
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        String placa = "ABC1234";
        VeiculoEntity entity = new VeiculoEntity(placa, "Ford", "Fiesta", 2020);
        Veiculo veiculo = new Veiculo(placa, "Ford", "Fiesta", 2020);

        when(repository.findById(placa)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(veiculo);

        Optional<Veiculo> resultado = adapter.buscarPorId(placa);

        assertTrue(resultado.isPresent());
        assertEquals(placa, resultado.get().getPlaca());
    }

    @Test
    void deveListarTodosComSucesso() {
        VeiculoEntity entity = new VeiculoEntity("ABC1234", "Ford", "Fiesta", 2020);
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Fiesta", 2020);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(veiculo);

        List<Veiculo> resultado = adapter.listarTodos();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveRemoverVeiculoComSucesso() {
        String placa = "ABC1234";

        adapter.remover(placa);

        verify(repository).deleteById(placa);
    }
}
