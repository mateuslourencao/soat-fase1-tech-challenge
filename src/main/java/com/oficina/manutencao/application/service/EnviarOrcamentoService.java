package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.EnviarOrcamentoUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class EnviarOrcamentoService extends TransicionarStatusOrdemDeServicoService implements EnviarOrcamentoUseCase {

    private final OrdemDeServicoRepositoryPort ordemDeServicoRepository;
    public EnviarOrcamentoService(OrdemDeServicoRepositoryPort ordemDeServicoRepository,
                                  ClienteRepositoryPort clienteRepository,
                                  NotificarClientePort notificarCliente) {
        super(ordemDeServicoRepository, clienteRepository, notificarCliente);
        this.ordemDeServicoRepository = ordemDeServicoRepository;
    }

    public OrdemDeServico enviarOrcamento(int id) {
        transicionar(id, StatusOS.AGUARDANDO_APROVACAO);

        OrdemDeServico os = ordemDeServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada após transição"));

        return os;
    }
}
