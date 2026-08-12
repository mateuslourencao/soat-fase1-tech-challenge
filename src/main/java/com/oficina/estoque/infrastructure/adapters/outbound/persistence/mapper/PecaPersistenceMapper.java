package com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PecaPersistenceMapper {
    public PecaEntity toEntity(Peca peca) {
        return new PecaEntity(peca.getId(), peca.getDescricao(), BigDecimal.valueOf(peca.getValor()), peca.getQuantidade());
    }

    public Peca toDomain(PecaEntity entity) {
        return new Peca(entity.getId(), entity.getDescricao(), entity.getValor().doubleValue(), entity.getQuantidade());
    }
}
