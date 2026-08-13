package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

public record ClienteResponseDTO(
        String nome,
        String email,
        String documento,
        String telefone
) {}