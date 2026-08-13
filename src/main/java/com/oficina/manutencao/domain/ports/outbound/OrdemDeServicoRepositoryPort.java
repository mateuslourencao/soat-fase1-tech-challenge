package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

import java.util.List;
import java.util.Optional;

public interface OrdemDeServicoRepositoryPort {
    OrdemDeServico salvar(OrdemDeServico ordemDeServico);
    Optional<OrdemDeServico> buscarPorId(int id);
    List<OrdemDeServico> listarTodos();
}
