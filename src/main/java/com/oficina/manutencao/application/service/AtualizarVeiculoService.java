package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.AtualizarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;

public class AtualizarVeiculoService implements AtualizarVeiculoUseCase {
    private final VeiculoRepositoryPort veiculoRepositoryPort;

    public  AtualizarVeiculoService(VeiculoRepositoryPort veiculoRepositoryPort) {
        this.veiculoRepositoryPort = veiculoRepositoryPort;
    }

    @Override
    public Veiculo atualizarVeiculo(String placa, Veiculo veiculo){
        Veiculo existente = veiculoRepositoryPort.buscarPorId(placa)
                .orElseThrow(() -> new RuntimeException("Veiculo não encontrado.")); // Ideal usar sua exceção de negócio

        Veiculo veiculoParaSalvar = new Veiculo(
                existente.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno()
        );

        return veiculoRepositoryPort.salvar(veiculoParaSalvar);
    }
}
