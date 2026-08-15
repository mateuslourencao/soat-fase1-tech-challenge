package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClientePersistenceMapper {
    public ClienteEntity toEntity(Cliente cliente) {
        String documentoLimpo = cliente.getDocumento().replaceAll("[^a-zA-Z0-9]", "");
        return new ClienteEntity(documentoLimpo, cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }

    public Cliente toDomain(ClienteEntity entity) {
        return new Cliente(entity.getDocumento(), entity.getNome(), entity.getEmail(), entity.getTelefone());
    }
}
