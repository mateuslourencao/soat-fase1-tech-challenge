package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.VeiculoEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VeiculoPersistenceMapperTest {
    private final VeiculoPersistenceMapper mapper = new VeiculoPersistenceMapper();

    @Test
    void deveConverterParaEntity() {
        Veiculo veiculo = new Veiculo("ABC1234", "Ford", "Fiesta", 2020);
        
        VeiculoEntity entity = mapper.toEntity(veiculo);
        
        assertEquals(veiculo.getPlaca(), entity.getPlaca());
        assertEquals(veiculo.getMarca(), entity.getMarca());
        assertEquals(veiculo.getModelo(), entity.getModelo());
        assertEquals(veiculo.getAno(), entity.getAno());
    }

    @Test
    void deveConverterParaDomain() {
        VeiculoEntity entity = new VeiculoEntity("ABC1234", "Ford", "Fiesta", 2020);
        
        Veiculo veiculo = mapper.toDomain(entity);
        
        assertEquals(entity.getPlaca(), veiculo.getPlaca());
        assertEquals(entity.getMarca(), veiculo.getMarca());
        assertEquals(entity.getModelo(), veiculo.getModelo());
        assertEquals(entity.getAno(), veiculo.getAno());
    }
}
