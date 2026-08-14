package com.oficina.estoque.domain.ports.inbound;

import com.oficina.estoque.domain.model.Servico;

public interface CadastrarServicoUseCase {
    Servico cadastrarServico(String descricao, Double valor);
}
