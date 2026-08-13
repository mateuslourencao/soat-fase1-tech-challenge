package com.oficina.administrativo.domain.ports.outbound;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;

import java.util.Optional;

public interface TokenJwtPort {
    String gerar(Funcionario funcionario);

    Optional<IdentidadeToken> validar(String token);

    record IdentidadeToken(int funcionarioId, PerfilFuncionario perfil) { }
}
