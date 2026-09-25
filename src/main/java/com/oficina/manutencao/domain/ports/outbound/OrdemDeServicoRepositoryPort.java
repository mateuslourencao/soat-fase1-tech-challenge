package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdemDeServicoRepositoryPort {
    OrdemDeServico salvar(OrdemDeServico ordemDeServico);
    Optional<OrdemDeServico> buscarPorId(int id);
    Optional<StatusOS> buscarStatusPorId(int id);
    List<OrdemDeServico> listarTodos();
    List<OrdemDeServico> buscarOrdensdeServicoPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
