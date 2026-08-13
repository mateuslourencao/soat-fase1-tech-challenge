package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Servico;

public interface AtualizarServicoUseCase {
    Servico atualizarServico(Servico servico);
}
