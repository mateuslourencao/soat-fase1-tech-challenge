package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.CadastrarClienteUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;

public class CadastrarClienteService implements CadastrarClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public CadastrarClienteService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepositoryPort.salvar(cliente);
    }
}
