package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import java.util.UUID;

abstract class TransicionarStatusOrdemDeServicoService {
    private final OrdemDeServicoRepositoryPort repositorio;

    protected TransicionarStatusOrdemDeServicoService(OrdemDeServicoRepositoryPort repositorio) {
        this.repositorio = repositorio;
    }

    protected void transicionar(UUID id, StatusOS origem, StatusOS destino) {
        OrdemDeServico ordem = repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));
        if (ordem.getStatus() != origem) {
            throw new IllegalStateException("Transição inválida: status atual " + ordem.getStatus());
        }
        ordem.alterarStatus(destino);
        repositorio.salvar(ordem);
    }
}
