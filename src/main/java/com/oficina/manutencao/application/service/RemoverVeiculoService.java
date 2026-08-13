package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.RemoverVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

import java.util.UUID;

public class RemoverVeiculoService implements RemoverVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public RemoverVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public void removerVeiculo(UUID id){
        veiculoRepositoryPort.buscarPorId(id).orElseThrow(() -> new RuntimeException("Veiculo não encontrado."));
        veiculoRepositoryPort.remover(id);
    }
}
