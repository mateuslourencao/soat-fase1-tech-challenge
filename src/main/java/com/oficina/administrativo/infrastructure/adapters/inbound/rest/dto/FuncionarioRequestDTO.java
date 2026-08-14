package com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto;

import com.oficina.administrativo.domain.model.PerfilFuncionario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FuncionarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String nome,
        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "Formato de e-mail inválido") String email,
        @NotBlank(message = "A senha é obrigatória") String senha,
        @NotNull(message = "O perfil é obrigatório") PerfilFuncionario perfil
) { }
