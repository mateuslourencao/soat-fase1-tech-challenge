package com.oficina.manutencao.domain.ports.inbound;

import java.util.UUID;

public interface RemoverVeiculoUseCase {
    void removerVeiculo(String placa);
}
