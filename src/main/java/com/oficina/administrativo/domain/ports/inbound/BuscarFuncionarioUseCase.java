package com.oficina.administrativo.domain.ports.inbound;

import com.oficina.administrativo.domain.model.Funcionario;

public interface BuscarFuncionarioUseCase {
    Funcionario buscarFuncionario(int id);
}
