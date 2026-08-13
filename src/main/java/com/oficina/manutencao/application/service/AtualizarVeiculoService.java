package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.AtualizarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

import java.util.UUID;

public class AtualizarVeiculoService implements AtualizarVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public  AtualizarVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public Veiculo atualizarVeiculo(UUID id, Veiculo veiculo){
        Veiculo existente = veiculoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Veiculo não encontrado.")); // Ideal usar sua exceção de negócio

        Veiculo veiculoParaSalvar = new Veiculo(
                existente.getId(),
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno()
        );

        return veiculoRepositoryPort.salvar(veiculoParaSalvar);
    }
}
