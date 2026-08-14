package com.oficina.administrativo.domain.ports.inbound;

import com.oficina.administrativo.domain.model.Funcionario;

import java.util.List;

public interface ListarFuncionariosUseCase {
    List<Funcionario> listarFuncionarios();
}
