package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.BuscarClienteUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;

import java.util.UUID;

public class BuscarClienteService implements BuscarClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public BuscarClienteService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public Cliente buscarCliente(String documento) {
        return clienteRepositoryPort.buscarPorId(documento).orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }
}
