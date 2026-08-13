package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Cliente;

public interface AtualizarClienteUseCase {
    Cliente atualizarCliente(String documento, Cliente cliente);
}
