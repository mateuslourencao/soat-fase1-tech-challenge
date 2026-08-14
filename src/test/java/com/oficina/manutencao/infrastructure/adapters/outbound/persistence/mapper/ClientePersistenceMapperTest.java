package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.ClienteEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientePersistenceMapperTest {
    private final ClientePersistenceMapper mapper = new ClientePersistenceMapper();

    @Test
    void deveConverterParaEntity() {
        Cliente cliente = new Cliente("12345678900", "João Silva", "joao@email.com", "11988887777");
        
        ClienteEntity entity = mapper.toEntity(cliente);
        
        assertEquals(cliente.getDocumento(), entity.getDocumento());
        assertEquals(cliente.getNome(), entity.getNome());
        assertEquals(cliente.getEmail(), entity.getEmail());
        assertEquals(cliente.getTelefone(), entity.getTelefone());
    }

    @Test
    void deveConverterParaDomain() {
        ClienteEntity entity = new ClienteEntity("12345678900", "João Silva", "joao@email.com", "11988887777");
        
        Cliente cliente = mapper.toDomain(entity);
        
        assertEquals(entity.getDocumento(), cliente.getDocumento());
        assertEquals(entity.getNome(), cliente.getNome());
        assertEquals(entity.getEmail(), cliente.getEmail());
        assertEquals(entity.getTelefone(), cliente.getTelefone());
    }
}
