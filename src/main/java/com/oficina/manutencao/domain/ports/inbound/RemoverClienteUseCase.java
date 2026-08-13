package com.oficina.manutencao.domain.ports.inbound;

import java.util.UUID;

public interface RemoverClienteUseCase {
    void removerCliente(String documento);
}
