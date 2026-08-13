package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.Veiculo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepositoryPort {
    Veiculo salvar(Veiculo veiculo);
    Optional<Veiculo> buscarPorId(UUID id);
    List<Veiculo> listarTodos();
    void remover(UUID id);
}
