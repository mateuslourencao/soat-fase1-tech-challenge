package com.oficina.manutencao.domain.ports.inbound;

import java.util.UUID;

public interface FinalizarReparoUseCase {
    void FinalizarReparo(UUID ordemDeServicoID);
}
