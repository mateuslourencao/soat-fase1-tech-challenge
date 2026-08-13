package com.oficina.administrativo.domain.ports.inbound;

import com.oficina.administrativo.domain.model.FuncionarioAutenticado;

public interface AutenticarFuncionarioUseCase {
    FuncionarioAutenticado autenticar(String email, String senha);
}
