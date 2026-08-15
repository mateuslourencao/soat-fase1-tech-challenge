package com.oficina.manutencao.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.domain.ports.outbound.ServicoRepositoryPort;
import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

import java.util.List;

public class AtualizarItensOrdemDeServicoService implements AtualizarItensOrdemDeServicoUseCase {
    private final OrdemDeServicoRepositoryPort repositorio;
    private final PecaRepositoryPort pecaRepositorio;
    private final ServicoRepositoryPort servicoRepositorio;

    public AtualizarItensOrdemDeServicoService(OrdemDeServicoRepositoryPort repositorio,
                                               PecaRepositoryPort pecaRepositorio,
                                               ServicoRepositoryPort servicoRepositorio) {
        this.repositorio = repositorio;
        this.pecaRepositorio = pecaRepositorio;
        this.servicoRepositorio = servicoRepositorio;
    }

    @Override
    public OrdemDeServico atualizarItensOrdemDeServico(int id, List<PecaItemInput> pecasInput, List<Integer> servicosIds) {
        if ((pecasInput == null || pecasInput.isEmpty()) && (servicosIds == null || servicosIds.isEmpty())) {
            throw new IllegalArgumentException("Informe ao menos uma peça necessária ou um serviço");
        }

        OrdemDeServico ordem = repositorio.buscarPorId(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
        if (ordem.getStatus() != StatusOS.EM_DIAGNOSTICO) {
            throw new IllegalStateException("Itens só podem ser atualizados durante o diagnóstico");
        }

        List<PecasNecessarias> pecas = pecasInput == null ? List.of() : pecasInput.stream()
                .map(input -> {
                    Peca peca = pecaRepositorio.buscarPorId(input.pecaId())
                            .orElseThrow(() -> new EntidadeNaoEncontradaException("Peça não encontrada: " + input.pecaId()));
                    return new PecasNecessarias(peca, input.quantidade());
                }).toList();

        List<Servico> servicos = servicosIds == null ? List.of() : servicosIds.stream()
                .map(servicoId -> servicoRepositorio.buscarPorId(servicoId)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado: " + servicoId)))
                .toList();

        ordem.registrarAtualizacaoDeItens(pecas, servicos);
        return repositorio.salvar(ordem);
    }
}
