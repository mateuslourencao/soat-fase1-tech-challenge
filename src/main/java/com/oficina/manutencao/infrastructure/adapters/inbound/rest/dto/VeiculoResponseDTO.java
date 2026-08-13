package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import java.util.UUID;

public record VeiculoResponseDTO(
        UUID id,
        String placa,
        String marca,
        String modelo,
        Integer ano
) {}
