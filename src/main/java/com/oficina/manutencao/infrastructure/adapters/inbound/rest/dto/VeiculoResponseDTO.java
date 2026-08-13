package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

public record VeiculoResponseDTO(
        String placa,
        String marca,
        String modelo,
        Integer ano
) {}
