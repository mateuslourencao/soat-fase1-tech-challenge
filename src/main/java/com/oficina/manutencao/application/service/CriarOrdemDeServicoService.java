package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.CriarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class CriarOrdemDeServicoService implements CriarOrdemDeServicoUseCase {
    private final OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort;

    public CriarOrdemDeServicoService(OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort) {
        this.ordemDeServicoRepositoryPort = ordemDeServicoRepositoryPort;
    }

    @Override
    public void criarOrdemDeServico(OrdemDeServico ordemDeServico) {
        //TODO validacoes de negocio
    }
}
