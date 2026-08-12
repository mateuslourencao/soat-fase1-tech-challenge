package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.VeiculoEntity;
import org.springframework.stereotype.Component;

@Component
public class VeiculoPersistenceMapper {
    public VeiculoEntity toEntity(Veiculo veiculo) {
        return new VeiculoEntity(veiculo.getId(), veiculo.getPlaca(), veiculo.getMarca(), veiculo.getModelo(), veiculo.getAno());
    }

    public Veiculo toDomain(VeiculoEntity entity) {
        return new Veiculo(entity.getId(), entity.getPlaca(), entity.getMarca(), entity.getModelo(), entity.getAno());
    }
}
