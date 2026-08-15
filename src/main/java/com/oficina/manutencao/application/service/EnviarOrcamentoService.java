package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.EnviarOrcamentoUseCase;
import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class EnviarOrcamentoService extends TransicionarStatusOrdemDeServicoService implements EnviarOrcamentoUseCase {

    private final OrdemDeServicoRepositoryPort ordemDeServicoRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final NotificarClientePort notificarCliente;

    public EnviarOrcamentoService(OrdemDeServicoRepositoryPort ordemDeServicoRepository,
                                  ClienteRepositoryPort clienteRepository,
                                  NotificarClientePort notificarCliente) {
        super(ordemDeServicoRepository);
        this.ordemDeServicoRepository = ordemDeServicoRepository;
        this.clienteRepository = clienteRepository;
        this.notificarCliente = notificarCliente;
    }

    public void enviarOrcamento(int id) {
        transicionar(id, StatusOS.EM_DIAGNOSTICO, StatusOS.AGUARDANDO_APROVACAO);

        OrdemDeServico os = ordemDeServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada após transição"));

        Cliente cliente = clienteRepository.buscarPorId(os.getDocumentoCliente())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado para a ordem de serviço"));

        notificarCliente.notificarOrcamentoAguardandoAprovacao(cliente, os);
    }
}
