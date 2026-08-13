package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(String documento);
    List<Cliente> listarTodos();
    void remover(String documento);
}
