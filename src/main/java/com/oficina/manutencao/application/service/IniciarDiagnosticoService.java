package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.IniciarDiagnosticoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import java.util.UUID;

public class IniciarDiagnosticoService extends TransicionarStatusOrdemDeServicoService implements IniciarDiagnosticoUseCase {
    public IniciarDiagnosticoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public void IniciarDiagnostico(UUID id) { transicionar(id, StatusOS.RECEBIDA, StatusOS.EM_DIAGNOSTICO); }
}
