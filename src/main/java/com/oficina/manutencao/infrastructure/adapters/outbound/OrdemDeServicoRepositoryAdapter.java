package com.oficina.manutencao.infrastructure.adapters.outbound;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OrdemDeServicoRepositoryAdapter implements OrdemDeServicoRepositoryPort {
    private final Map<UUID, OrdemDeServico> ordens = new ConcurrentHashMap<>();

    @Override
    public OrdemDeServico salvar(OrdemDeServico ordemDeServico) {
        ordens.put(ordemDeServico.getId(), ordemDeServico);
        return ordemDeServico;
    }

    @Override
    public Optional<OrdemDeServico> buscarPorId(UUID id) {
        return Optional.ofNullable(ordens.get(id));
    }

    @Override
    public List<OrdemDeServico> listarOrdensDeServico() {
        return List.of();
    }
}
