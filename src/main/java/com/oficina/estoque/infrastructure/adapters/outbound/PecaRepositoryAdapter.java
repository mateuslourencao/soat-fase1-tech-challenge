package com.oficina.estoque.infrastructure.adapters.outbound;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PecaRepositoryAdapter implements PecaRepositoryPort {
    private final Map<UUID, Peca> pecas = new ConcurrentHashMap<>();
    public Peca salvar(Peca peca) { pecas.put(peca.getId(), peca); return peca; }
    public Optional<Peca> buscarPorId(UUID id) { return Optional.ofNullable(pecas.get(id)); }
}
