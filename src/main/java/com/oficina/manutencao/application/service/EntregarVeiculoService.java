package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.EntregarVeiculoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

public class EntregarVeiculoService extends TransicionarStatusOrdemDeServicoService implements EntregarVeiculoUseCase {
    public EntregarVeiculoService(OrdemDeServicoRepositoryPort repositorio) { super(repositorio); }
    public void entregarVeiculo(int id) { transicionar(id, StatusOS.FINALIZADA, StatusOS.ENTREGUE); }
}
