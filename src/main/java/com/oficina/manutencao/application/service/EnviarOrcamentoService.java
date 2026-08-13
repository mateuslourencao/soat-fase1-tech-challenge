package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.EnviarOrcamentoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class EnviarOrcamentoService extends TransicionarStatusOrdemDeServicoService implements EnviarOrcamentoUseCase {
    public EnviarOrcamentoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public void EnviarOrcamento(int id) { transicionar(id, StatusOS.EM_DIAGNOSTICO, StatusOS.AGUARDANDO_APROVACAO); }
}
