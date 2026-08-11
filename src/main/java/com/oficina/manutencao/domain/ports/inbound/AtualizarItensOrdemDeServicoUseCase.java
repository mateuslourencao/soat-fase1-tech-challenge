package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import java.util.List;
import java.util.UUID;

public interface AtualizarItensOrdemDeServicoUseCase {
    OrdemDeServico AtualizarOrdemDeServico(UUID ordemDeServicoID, List<PecasNecessarias> pecasNecessarias, List<Servico> servicos);
}
