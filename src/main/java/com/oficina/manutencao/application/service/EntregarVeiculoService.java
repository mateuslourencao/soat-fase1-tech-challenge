package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.EntregarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;
import com.oficina.manutencao.domain.ports.outbound.NotificarClientePort;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class EntregarVeiculoService extends TransicionarStatusOrdemDeServicoService implements EntregarVeiculoUseCase {
    public EntregarVeiculoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public EntregarVeiculoService(OrdemDeServicoRepositoryPort repositorio,
                                  ClienteRepositoryPort clienteRepositorio,
                                  NotificarClientePort notificarCliente) {
        super(repositorio, clienteRepositorio, notificarCliente);
    }
    public void entregarVeiculo(int id) { transicionar(id, StatusOS.ENTREGUE); }
}
