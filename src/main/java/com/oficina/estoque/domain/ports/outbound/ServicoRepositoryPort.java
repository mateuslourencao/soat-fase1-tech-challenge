package com.oficina.estoque.domain.ports.outbound;

import com.oficina.estoque.domain.model.Servico;

public interface ServicoRepositoryPort {
    Servico salvar(Servico servico);
}
