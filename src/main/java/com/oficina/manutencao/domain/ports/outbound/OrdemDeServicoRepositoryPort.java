package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

public interface OrdemDeServicoRepositoryPort {
    OrdemDeServico salvar(OrdemDeServico ordemDeServico);
}
