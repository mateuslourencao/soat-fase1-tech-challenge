package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.ports.inbound.AtualizarClienteUseCase;
import com.oficina.manutencao.domain.ports.outbound.ClienteRepositoryPort;

import java.util.UUID;

public class AtualizarClienteService implements AtualizarClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;

    public AtualizarClienteService(ClienteRepositoryPort clienteRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public Cliente atualizarCliente(UUID id, Cliente cliente) {
        Cliente existente = clienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado.")); // Ideal usar sua exceção de negócio

        Cliente clienteParaSalvar = new Cliente(
                existente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDocumento(),
                cliente.getTelefone()
        );

        return clienteRepositoryPort.salvar(clienteParaSalvar);
    }
}
