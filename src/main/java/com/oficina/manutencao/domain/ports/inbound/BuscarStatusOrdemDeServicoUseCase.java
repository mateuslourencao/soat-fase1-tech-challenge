package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.StatusOS;

public interface BuscarStatusOrdemDeServicoUseCase {
    StatusOS buscarStatusOrdemDeServico(int id);
}
