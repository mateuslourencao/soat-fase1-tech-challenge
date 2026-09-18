package com.oficina.manutencao.application.service;

import com.oficina.common.domain.exception.EntidadeNaoEncontradaException;
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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));

        return clienteRepositoryPort.salvar(
                existente.atualizarDados(cliente.getNome(), cliente.getEmail(), cliente.getTelefone())
        );
    }
}
