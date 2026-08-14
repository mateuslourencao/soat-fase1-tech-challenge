package com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PecaPersistenceMapperTest {
    private final PecaPersistenceMapper mapper = new PecaPersistenceMapper();

    @Test void deveConverterParaEntity() {
        Peca peca = new Peca(1, "Filtro", 30.0, 10);
        PecaEntity entity = mapper.toEntity(peca);
        
        assertEquals(1, entity.getId());
        assertEquals("Filtro", entity.getDescricao());
        assertEquals(BigDecimal.valueOf(30.0), entity.getValor());
        assertEquals(10, entity.getQuantidade());
    }

    @Test void deveConverterParaDomain() {
        PecaEntity entity = new PecaEntity(1, "Filtro", BigDecimal.valueOf(30.0), 10);
        Peca peca = mapper.toDomain(entity);
        
        assertEquals(1, peca.getId());
        assertEquals("Filtro", peca.getDescricao());
        assertEquals(30.0, peca.getValor());
        assertEquals(10, peca.getQuantidade());
    }
}
