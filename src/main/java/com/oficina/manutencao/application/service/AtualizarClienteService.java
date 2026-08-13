package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.AtualizarClienteUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;

public class AtualizarClienteService implements AtualizarClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public AtualizarClienteService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public Cliente atualizarCliente(String documento, Cliente cliente) {
        Cliente existente = clienteRepositoryPort.buscarPorId(documento)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        Cliente clienteParaSalvar = new Cliente(
                existente.getDocumento(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
        );

        return clienteRepositoryPort.salvar(clienteParaSalvar);
    }
}
