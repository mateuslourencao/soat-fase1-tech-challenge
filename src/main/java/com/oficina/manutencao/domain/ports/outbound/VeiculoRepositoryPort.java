package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepositoryPort {
    Veiculo salvar(Veiculo veiculo);
    Optional<Veiculo> buscarPorId(String placa);
    List<Veiculo> listarTodos();
    void remover(String placa);
}
