package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AprovarOrcamentoUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class AprovarOrcamentoService extends TransicionarStatusOrdemDeServicoService implements AprovarOrcamentoUseCase {
    public AprovarOrcamentoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public AprovarOrcamentoService(OrdemDeServicoRepositoryPort repositorio,
                                   ClienteRepositoryPort clienteRepositorio,
                                   NotificarClientePort notificarCliente) {
        super(repositorio, clienteRepositorio, notificarCliente);
    }

    @Override
    public void aprovarOrcamento(int id, boolean aprovado) {
        transicionar(id, StatusOS.AGUARDANDO_APROVACAO,
                aprovado ? StatusOS.EM_EXECUCAO : StatusOS.FINALIZADA);
    }
}
