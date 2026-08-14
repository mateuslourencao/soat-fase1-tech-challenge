package com.oficina.administrativo.domain.ports.inbound;

import com.oficina.administrativo.domain.model.Funcionario;

public interface AtualizarFuncionarioUseCase {
    Funcionario atualizarFuncionario(int id, Funcionario funcionario);
}
