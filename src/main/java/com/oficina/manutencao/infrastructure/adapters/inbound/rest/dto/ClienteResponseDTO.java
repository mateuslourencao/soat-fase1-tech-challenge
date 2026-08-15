package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

public record ClienteResponseDTO(
        String documento,
        String nome,
        String email,
        String telefone
) {}