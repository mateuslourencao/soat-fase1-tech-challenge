package com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto;

import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.model.PerfilFuncionario;

public record FuncionarioAutenticadoResponseDTO(int id, String nome, String email, PerfilFuncionario perfil, String token) {
    public FuncionarioAutenticadoResponseDTO(FuncionarioAutenticado autenticacao) {
        this(autenticacao.funcionario().getId(), autenticacao.funcionario().getNome(),
                autenticacao.funcionario().getEmail(), autenticacao.funcionario().getPerfil(), autenticacao.token());
    }
}
