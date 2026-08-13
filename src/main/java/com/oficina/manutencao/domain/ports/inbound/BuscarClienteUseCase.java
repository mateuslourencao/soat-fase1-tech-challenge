package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Cliente;

public interface BuscarClienteUseCase {
    Cliente buscarCliente(String documento);
}
