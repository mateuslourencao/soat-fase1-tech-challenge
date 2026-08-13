package com.oficina.estoque.infrastructure.adapters.inbound.rest.dto;

import com.oficina.estoque.domain.model.Peca;

public record PecaResponseDTO(
        int id,
        String descricao,
        double valor,
        int quantidade
) {
    public PecaResponseDTO(Peca peca) {
        this(peca.getId(), peca.getDescricao(), peca.getValor(), peca.getQuantidade());
    }
}
