package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

import java.util.List;

public class ListarOrdensDeServicoService implements ListarOrdensDeServicoUseCase {

    private final OrdemDeServicoRepositoryPort ordemDeServicoRepository;

    public ListarOrdensDeServicoService(OrdemDeServicoRepositoryPort ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    @Override
    public List<OrdemDeServico> listarOrdensDeServico() {
        return ordemDeServicoRepository.listarTodos();
    }

}
