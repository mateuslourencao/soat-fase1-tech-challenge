package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {
    public ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getDocumento(), cliente.getTelefone());
    }

    public Cliente toDomain(ClienteEntity entity) {
        return new Cliente(entity.getId(), entity.getNome(), entity.getEmail(), entity.getDocumento(), entity.getTelefone());
    }
}
