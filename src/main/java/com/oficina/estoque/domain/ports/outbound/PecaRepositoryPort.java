package com.oficina.estoque.domain.ports.outbound;

import com.oficina.estoque.domain.model.Peca;
import java.util.Optional;
import java.util.UUID;

public interface PecaRepositoryPort {
    Peca salvar(Peca peca);
    Optional<Peca> buscarPorId(UUID id);
}
