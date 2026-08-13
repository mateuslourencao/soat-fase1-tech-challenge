package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.ports.inbound.RemoverClienteUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;

import java.util.UUID;

public class RemoverClienteService implements RemoverClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public RemoverClienteService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public void removerCliente(String documento){
        clienteRepositoryPort.buscarPorId(documento).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        clienteRepositoryPort.remover(documento);
    }
}
