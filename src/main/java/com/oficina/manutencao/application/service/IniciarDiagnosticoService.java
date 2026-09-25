package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.IniciarDiagnosticoUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class IniciarDiagnosticoService extends TransicionarStatusOrdemDeServicoService implements IniciarDiagnosticoUseCase {
    public IniciarDiagnosticoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public IniciarDiagnosticoService(OrdemDeServicoRepositoryPort repositorio,
                                     ClienteRepositoryPort clienteRepositorio,
                                     NotificarClientePort notificarCliente) {
        super(repositorio, clienteRepositorio, notificarCliente);
    }
    public void iniciarDiagnostico(int id) { transicionar(id, StatusOS.EM_DIAGNOSTICO); }
}
