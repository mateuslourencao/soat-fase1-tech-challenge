package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.ports.inbound.RemoverVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

public class RemoverVeiculoService implements RemoverVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public RemoverVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public void removerVeiculo(String placa){
        veiculoRepositoryPort.buscarPorId(placa).orElseThrow(() -> new RuntimeException("Veiculo não encontrado."));
        veiculoRepositoryPort.remover(placa);
    }
}
