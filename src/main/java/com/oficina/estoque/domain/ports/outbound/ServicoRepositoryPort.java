package com.oficina.estoque.domain.ports.outbound;

import com.oficina.estoque.domain.model.Servico;

import java.util.Optional;
import java.util.UUID;

public interface ServicoRepositoryPort {
    Servico salvar(Servico servico);
    Optional<Servico> buscarPorId(UUID id);
}
