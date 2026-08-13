package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.ListarVeiculosUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

import java.util.List;

public class ListarVeiculosService implements ListarVeiculosUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public ListarVeiculosService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public List<Veiculo> listarVeiculos(){
        return veiculoRepositoryPort.listarTodos();
    }
}
