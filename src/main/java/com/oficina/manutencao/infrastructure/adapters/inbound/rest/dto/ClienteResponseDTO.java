package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import java.util.UUID;

public record ClienteResponseDTO(
        String nome,
        String email,
        String documento,
        String telefone
) {}