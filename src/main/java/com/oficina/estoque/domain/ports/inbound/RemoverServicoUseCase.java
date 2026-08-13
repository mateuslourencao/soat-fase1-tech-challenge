package com.oficina.estoque.domain.ports.inbound;

import java.util.UUID;

public interface RemoverServicoUseCase {
    Void removerServico(int idServico);
}
