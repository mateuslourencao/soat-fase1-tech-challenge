package com.oficina.manutencao.domain.ports.inbound;

import com.oficina.manutencao.domain.model.Cliente;

import java.util.List;

public interface ListarClientesUseCase {
    List<Cliente> listarClientes();
}
