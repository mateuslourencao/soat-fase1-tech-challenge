package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import com.oficina.estoque.domain.model.Servico;

public record ServicoResponseDTO(
        int id,
        String descricao,
        double valor
) {
    public ServicoResponseDTO(Servico servico) {
        this(servico.getId(), servico.getDescricao(), servico.getValor());
    }
}
