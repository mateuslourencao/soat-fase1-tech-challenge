package com.oficina.administrativo.domain.ports.inbound;

import com.oficina.administrativo.domain.model.Funcionario;

public interface CadastrarFuncionarioUseCase {
    Funcionario cadastrarFuncionario(Funcionario funcionario, String senha);
}
