package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Veiculo;

import java.util.List;

public interface ListarVeiculosUseCase {
    List<Veiculo> listarVeiculos();
}
