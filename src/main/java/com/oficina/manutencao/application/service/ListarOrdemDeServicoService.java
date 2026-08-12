package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.OrdemDeServicoJpaAdapter;

import java.util.List;

public class ListarOrdemDeServicoService implements ListarOrdemDeServicoUseCase {

    private final OrdemDeServicoRepositoryPort ordemDeServicoRepository;

    public ListarOrdemDeServicoService(OrdemDeServicoRepositoryPort ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    @Override
    public List<OrdemDeServico> listarOrdemDeServico() {
        List<OrdemDeServico> ordensDeServico = ordemDeServicoRepository.listarOrdensDeServico();
        return ordensDeServico;
    }

}
