package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.BuscarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

public class BuscarVeiculoService implements BuscarVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public BuscarVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public Veiculo buscarVeiculo(String placa) {
        return veiculoRepositoryPort.buscarPorId(placa).orElseThrow(() -> new EntidadeNaoEncontradaException("Veiculo não encontrado."));
    }
}
