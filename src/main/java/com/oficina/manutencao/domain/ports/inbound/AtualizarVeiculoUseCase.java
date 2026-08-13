package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Veiculo;

import java.util.UUID;

public interface AtualizarVeiculoUseCase {
    Veiculo atualizarVeiculo(UUID id, Veiculo veiculo);
}
