package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AprovarOrcamentoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class AprovarOrcamentoService extends TransicionarStatusOrdemDeServicoService implements AprovarOrcamentoUseCase {
    public AprovarOrcamentoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }

    @Override
    public void aprovarOrcamento(int id, boolean aprovado) {
        transicionar(id, StatusOS.AGUARDANDO_APROVACAO,
                aprovado ? StatusOS.EM_EXECUCAO : StatusOS.FINALIZADA);
    }
}
