package com.oficina.estoque.domain.ports.outbound;

import com.oficina.estoque.domain.model.Peca;

import java.util.List;
import java.util.Optional;


public interface PecaRepositoryPort {
    Peca salvar(Peca peca);
    Optional<Peca> buscarPorId(int id);

    List<Peca> listarPecas();
    void deletar(int id);
}
