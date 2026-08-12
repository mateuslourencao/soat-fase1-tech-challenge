package com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ServicoPersistenceMapper {
    public ServicoEntity toEntity(Servico servico) {
        return new ServicoEntity(servico.getId(), servico.getDescricao(), BigDecimal.valueOf(servico.getValor()));
    }

    public Servico toDomain(ServicoEntity entity) {
        return new Servico(entity.getId(), entity.getDescricao(), entity.getValor().doubleValue());
    }
}
