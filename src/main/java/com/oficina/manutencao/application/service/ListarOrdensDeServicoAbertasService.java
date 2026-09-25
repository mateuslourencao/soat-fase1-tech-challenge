package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoAbertasUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

import java.util.List;

public class ListarOrdensDeServicoAbertasService implements ListarOrdensDeServicoAbertasUseCase {
    private final OrdemDeServicoRepositoryPort ordemDeServicoRepository;

    public ListarOrdensDeServicoAbertasService(OrdemDeServicoRepositoryPort ordemDeServicoRepository) {
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    @Override
    public List<OrdemDeServico> listarOrdensDeServicoAbertas() {
        return ordemDeServicoRepository.listarOrdenadoEmAberto();
    }

}
