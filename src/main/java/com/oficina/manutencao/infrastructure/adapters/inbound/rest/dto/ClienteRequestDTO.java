package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O documento é obrigatório")
        String documento,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone
) {}