package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

public interface BuscarOrdemDeServicoUseCase {
    OrdemDeServico buscarOrdemDeServico(int id);
}
