package com.oficina.estoque.infrastructure.adapters.outbound;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServicoRepositoryAdapter implements ServicoRepositoryPort {
    private final Map<UUID, Servico> servicos = new ConcurrentHashMap<>();
    public Servico salvar(Servico servico) { servicos.put(servico.getId(), servico); return servico; }
    public Optional<Servico> buscarPorId(UUID id) { return Optional.ofNullable(servicos.get(id)); }

    @Override
    public List<Servico> listarServicos() {
        return List.of();
    }
}
