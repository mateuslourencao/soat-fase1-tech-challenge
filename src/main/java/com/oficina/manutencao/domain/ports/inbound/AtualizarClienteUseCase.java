package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Cliente;

import java.util.UUID;

public interface AtualizarClienteUseCase {
    Cliente atualizarCliente(UUID id, Cliente cliente);
}
