package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepositoryPort {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(UUID id);
    List<Cliente> listarTodos();
    void remover(UUID id);
}
