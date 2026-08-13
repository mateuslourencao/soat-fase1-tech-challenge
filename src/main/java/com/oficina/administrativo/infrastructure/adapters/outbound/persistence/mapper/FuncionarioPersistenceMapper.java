package com.oficina.administrativo.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.entity.FuncionarioEntity;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioPersistenceMapper {
    public Funcionario toDomain(FuncionarioEntity entity) {
        return new Funcionario(entity.getId(), entity.getNome(), entity.getEmail(), entity.getSenhaHash(), entity.getPerfil(), entity.isAtivo());
    }
}
