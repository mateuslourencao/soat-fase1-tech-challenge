package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.FinalizarReparoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class FinalizarReparoService extends TransicionarStatusOrdemDeServicoService implements FinalizarReparoUseCase {
    public FinalizarReparoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public void FinalizarReparo(int id) { transicionar(id, StatusOS.EM_EXECUCAO, StatusOS.FINALIZADA); }
}
