package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Servico;

public interface BuscarServicoUseCase {
    Servico buscarServico(int idServico);
}
