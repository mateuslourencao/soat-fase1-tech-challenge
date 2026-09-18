package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AprovarOrcamentoClienteUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class AprovarOrcamentoClienteService extends TransicionarStatusOrdemDeServicoService
        implements AprovarOrcamentoClienteUseCase {

    private final OrdemDeServicoRepositoryPort repositorio;

    public AprovarOrcamentoClienteService(OrdemDeServicoRepositoryPort repositorio) {
        super(repositorio);
        this.repositorio = repositorio;
    }

    @Override
    public void aprovarOrcamento(int ordemDeServicoId, String documentoCliente, boolean aprovado) {
        if (documentoCliente == null || documentoCliente.isBlank()) {
            throw new IllegalArgumentException("Documento do cliente é obrigatório");
        }

        OrdemDeServico ordem = repositorio.buscarPorId(ordemDeServicoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada"));
        String documentoNormalizado = documentoCliente.replaceAll("[^a-zA-Z0-9]", "");

        if (!ordem.getDocumentoCliente().equals(documentoNormalizado)) {
            throw new IllegalArgumentException("Documento do cliente não pertence à ordem de serviço");
        }

        transicionar(ordemDeServicoId, StatusOS.AGUARDANDO_APROVACAO,
                aprovado ? StatusOS.EM_EXECUCAO : StatusOS.FINALIZADA);
    }
}
