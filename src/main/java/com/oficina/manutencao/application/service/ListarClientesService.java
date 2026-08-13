package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.ListarClientesUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;

import java.util.List;

public class ListarClientesService implements ListarClientesUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public ListarClientesService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    public List<Cliente> listarClientes(){
        return clienteRepositoryPort.listarTodos();
    }
}
