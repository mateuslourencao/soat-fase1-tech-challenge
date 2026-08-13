package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Peca;

public record PecasNecessarias(Peca peca, int quantidade) {
    public PecasNecessarias {
        if (peca == null || quantidade <= 0) {
            throw new IllegalArgumentException("Peça e quantidade positiva são obrigatórias");
        }
    }

    public double getValorTotal() {
        return peca.getValor() * quantidade;
    }

    public double getValorUnitario() {
        return peca.getValor();
    }
}
