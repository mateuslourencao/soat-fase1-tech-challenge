package com.oficina.manutencao.domain.ports.inbound;


import com.oficina.manutencao.domain.model.Cliente;

public interface CadastrarClienteUseCase {
   Cliente cadastrarCliente(Cliente cliente);
}
