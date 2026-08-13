package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.BuscarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

import java.util.UUID;

public class BuscarVeiculoService implements BuscarVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public BuscarVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public Veiculo buscarVeiculo(String placa) {
        return veiculoRepositoryPort.buscarPorId(placa).orElseThrow(() -> new RuntimeException("Veiculo não encontrado."));
    }
}
