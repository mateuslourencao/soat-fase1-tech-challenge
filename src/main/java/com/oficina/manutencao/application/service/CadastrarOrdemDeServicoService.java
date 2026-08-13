package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.CadastrarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class CadastrarOrdemDeServicoService implements CadastrarOrdemDeServicoUseCase {
    private final OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort;

    public CadastrarOrdemDeServicoService(OrdemDeServicoRepositoryPort ordemDeServicoRepositoryPort) {
        this.ordemDeServicoRepositoryPort = ordemDeServicoRepositoryPort;
    }

    @Override
    public OrdemDeServico cadastrarOrdemDeServico(OrdemDeServico ordemDeServico) {
        if (ordemDeServico == null) {
            throw new IllegalArgumentException("Ordem de serviço inválida");
        }
        return ordemDeServicoRepositoryPort.salvar(ordemDeServico);
    }

}
