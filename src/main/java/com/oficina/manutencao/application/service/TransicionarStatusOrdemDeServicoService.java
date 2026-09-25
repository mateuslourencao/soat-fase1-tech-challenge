package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class TransicionarStatusOrdemDeServicoService {
    private static final Logger logger = LoggerFactory.getLogger(TransicionarStatusOrdemDeServicoService.class);
    private final OrdemDeServicoRepositoryPort repositorio;
    private final ClienteRepositoryPort clienteRepositorio;
    private final NotificarClientePort notificarCliente;

    protected TransicionarStatusOrdemDeServicoService(OrdemDeServicoRepositoryPort repositorio) {
        this(repositorio, null, null);
    }

    protected TransicionarStatusOrdemDeServicoService(OrdemDeServicoRepositoryPort repositorio,
                                                       ClienteRepositoryPort clienteRepositorio,
                                                       NotificarClientePort notificarCliente) {
        this.repositorio = repositorio;
        this.clienteRepositorio = clienteRepositorio;
        this.notificarCliente = notificarCliente;
    }

    protected void transicionar(int id, StatusOS destino) {
        OrdemDeServico ordem = repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
        ordem.transicionarPara(destino);
        repositorio.salvar(ordem);
        notificarAtualizacaoStatus(ordem);
    }

    protected void transicionar(int id, StatusOS origem, StatusOS destino) {
        OrdemDeServico ordem = repositorio.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
        if (ordem.getStatus() != origem) {
            throw new IllegalStateException("Transição inválida: status atual " + ordem.getStatus());
        }
        ordem.transicionarPara(destino);
        repositorio.salvar(ordem);
        notificarAtualizacaoStatus(ordem);
    }

    private void notificarAtualizacaoStatus(OrdemDeServico ordem) {
        if (clienteRepositorio == null || notificarCliente == null) {
            return;
        }

        try {
            Cliente cliente = clienteRepositorio.buscarPorId(ordem.getDocumentoCliente())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException(
                            "Cliente não encontrado para a ordem de serviço"));
            notificarCliente.notificarAtualizacaoStatus(cliente, ordem);
        } catch (RuntimeException exception) {
            logger.error("Não foi possível enviar a atualização da OS #{} por e-mail", ordem.getId(), exception);
        }
    }
}
