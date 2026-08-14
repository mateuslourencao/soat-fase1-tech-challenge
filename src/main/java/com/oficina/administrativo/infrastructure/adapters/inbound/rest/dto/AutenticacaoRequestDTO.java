package com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AutenticacaoRequestDTO(
        @Schema(description = "E-mail do funcionário", example = "admin@oficina.com")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @Schema(description = "Senha de acesso", example = "123456")
        @NotBlank(message = "A senha é obrigatória")
        String senha
) { }
