package com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.model.PerfilFuncionario;

public record FuncionarioResponseDTO(int id, String nome, String email, PerfilFuncionario perfil, boolean ativo) {
    public FuncionarioResponseDTO(Funcionario funcionario) {
        this(funcionario.getId(), funcionario.getNome(), funcionario.getEmail(), funcionario.getPerfil(), funcionario.isAtivo());
    }
}
