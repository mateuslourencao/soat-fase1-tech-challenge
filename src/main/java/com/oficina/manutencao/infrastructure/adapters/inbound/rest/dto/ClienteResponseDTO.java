package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import java.util.UUID;

public record ClienteResponseDTO(
        UUID id,
        String nome,
        String email,
        String documento,
        String telefone
) {}