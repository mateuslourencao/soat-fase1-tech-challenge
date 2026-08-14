package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

import java.util.List;

public interface AtualizarItensOrdemDeServicoUseCase {
    OrdemDeServico atualizarItensOrdemDeServico(int ordemDeServicoID, List<PecaItemInput> pecasNecessarias, List<Integer> servicosIds);

    record PecaItemInput(int pecaId, int quantidade) {}
}
