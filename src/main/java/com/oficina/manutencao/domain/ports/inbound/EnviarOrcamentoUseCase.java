package com.oficina.manutencao.domain.ports.inbound;

import java.util.UUID;

public interface EnviarOrcamentoUseCase {
    void EnviarOrcamento(UUID ordemDeServicoID);
}
