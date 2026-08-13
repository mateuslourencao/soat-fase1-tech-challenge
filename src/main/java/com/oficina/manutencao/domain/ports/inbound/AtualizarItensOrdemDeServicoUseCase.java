package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;

import java.util.List;

public interface AtualizarItensOrdemDeServicoUseCase {
    OrdemDeServico AtualizarOrdemDeServico(int ordemDeServicoID, List<PecasNecessarias> pecasNecessarias, List<Servico> servicos);
}
