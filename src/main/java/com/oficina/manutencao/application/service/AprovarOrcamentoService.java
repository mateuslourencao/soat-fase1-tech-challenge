package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AprovarOrcamentoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import java.util.UUID;

public class AprovarOrcamentoService extends TransicionarStatusOrdemDeServicoService implements AprovarOrcamentoUseCase {
    public AprovarOrcamentoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public void AprovarOrcamento(UUID id) { transicionar(id, StatusOS.AGUARDANDO_APROVACAO, StatusOS.EM_EXECUCAO); }
}
