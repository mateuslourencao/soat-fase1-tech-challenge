package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class BuscarOrdemDeServicoService implements BuscarOrdemDeServicoUseCase {
    private final OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort;

    public BuscarOrdemDeServicoService(OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort) {
        this.ordemDeServicoRepositoryPort = ordemDeServicoRepositoryPort;
    }

    @Override
    public OrdemDeServico buscarOrdemDeServico(int id) {
        return ordemDeServicoRepositoryPort.buscarPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("OS não encontrada."));
    }
}
