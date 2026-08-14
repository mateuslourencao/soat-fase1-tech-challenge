package com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServicoPersistenceMapperTest {
    private final ServicoPersistenceMapper mapper = new ServicoPersistenceMapper();

    @Test void deveConverterParaEntity() {
        Servico servico = new Servico(1, "Troca de Oleo", 150.0);
        ServicoEntity entity = mapper.toEntity(servico);
        
        assertEquals(1, entity.getId());
        assertEquals("Troca de Oleo", entity.getDescricao());
        assertEquals(BigDecimal.valueOf(150.0), entity.getValor());
    }

    @Test void deveConverterParaDomain() {
        ServicoEntity entity = new ServicoEntity(1, "Troca de Oleo", BigDecimal.valueOf(150.0));
        Servico servico = mapper.toDomain(entity);
        
        assertEquals(1, servico.getId());
        assertEquals("Troca de Oleo", servico.getDescricao());
        assertEquals(150.0, servico.getValor());
    }
}
