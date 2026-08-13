package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Veiculo;

public interface BuscarVeiculoUseCase {
    Veiculo buscarVeiculo(String placa);
}
