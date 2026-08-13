package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.CadastrarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

public class CadastrarVeiculoService implements CadastrarVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public CadastrarVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public Veiculo cadastrarVeiculo(Veiculo veiculo){
        return veiculoRepositoryPort.salvar(veiculo);
    }
}
