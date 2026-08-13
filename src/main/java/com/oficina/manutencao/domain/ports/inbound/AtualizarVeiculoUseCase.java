package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Veiculo;

public interface AtualizarVeiculoUseCase {
    Veiculo atualizarVeiculo(String placa, Veiculo veiculo);
}
