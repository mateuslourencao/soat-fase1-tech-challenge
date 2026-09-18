package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

abstract class TransicionarStatusOrdemDeServicoService {
    private final OrdemDeServicoRepositoryPort repositorio;

    protected TransicionarStatusOrdemDeServicoService(OrdemDeServicoRepositoryPort repositorio) {
        this.repositorio = repositorio;
    }

    protected void transicionar(int id, StatusOS destino) {
        OrdemDeServico ordem = repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
        ordem.transicionarPara(destino);
        repositorio.salvar(ordem);
    }
}
