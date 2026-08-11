package com.oficina.manutencao.infrastructure.adapters.outbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class OrdemDeServicoRepositoryAdapter implements OrdemDeServicoRepositoryPort {
    @Override
    public OrdemDeServico salvar(OrdemDeServico ordemDeServico) {
        return null;
    }
}
