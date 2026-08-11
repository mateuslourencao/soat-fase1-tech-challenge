package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;

import java.util.Optional;
import java.util.UUID;

public interface OrdemDeServicoRepositoryPort {
    OrdemDeServico salvar(OrdemDeServico ordemDeServico);
    Optional<OrdemDeServico> buscarPorId(UUID id);
}
