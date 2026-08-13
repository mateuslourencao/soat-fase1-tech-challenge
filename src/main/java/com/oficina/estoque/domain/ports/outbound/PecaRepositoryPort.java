package com.oficina.estoque.domain.ports.outbound;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PecaRepositoryPort {
    Peca salvar(Peca peca);
    Optional<Peca> buscarPorId(int id);

    List<Peca> listarPecas();
}
