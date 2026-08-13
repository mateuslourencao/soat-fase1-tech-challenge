package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Veiculo;

public interface CadastrarVeiculoUseCase {
    Veiculo cadastrarVeiculo(Veiculo veiculo);
}
