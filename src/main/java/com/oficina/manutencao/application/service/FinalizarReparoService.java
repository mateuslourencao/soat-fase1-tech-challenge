package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.FinalizarReparoUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class FinalizarReparoService extends TransicionarStatusOrdemDeServicoService implements FinalizarReparoUseCase {
    public FinalizarReparoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public FinalizarReparoService(OrdemDeServicoRepositoryPort repositorio,
                                  ClienteRepositoryPort clienteRepositorio,
                                  NotificarClientePort notificarCliente) {
        super(repositorio, clienteRepositorio, notificarCliente);
    }
    public void finalizarReparo(int id) { transicionar(id, StatusOS.FINALIZADA); }
}
