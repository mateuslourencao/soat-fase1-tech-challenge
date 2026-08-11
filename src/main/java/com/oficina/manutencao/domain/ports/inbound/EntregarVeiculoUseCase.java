package com.oficina.manutencao.domain.ports.inbound;

import java.util.UUID;

public interface EntregarVeiculoUseCase {
    void EntregarVeiculo(UUID ordemDeServicoID);
}
