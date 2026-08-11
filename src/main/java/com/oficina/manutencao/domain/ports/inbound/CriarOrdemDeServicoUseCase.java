package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

public interface CriarOrdemDeServicoUseCase {

    void criarOrdemDeServico(OrdemDeServico ordemDeServico);
}
