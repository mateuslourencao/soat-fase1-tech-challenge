package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.BuscarStatusOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class BuscarStatusOrdemDeServicoService implements BuscarStatusOrdemDeServicoUseCase {
    private final OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort;

    public BuscarStatusOrdemDeServicoService(OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort) {
        this.ordemDeServicoRepositoryPort = ordemDeServicoRepositoryPort;
    }

    @Override
    public StatusOS buscarStatusOrdemDeServico(int id) {
        return ordemDeServicoRepositoryPort.buscarStatusPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("OS não encontrada."));
    }
}
