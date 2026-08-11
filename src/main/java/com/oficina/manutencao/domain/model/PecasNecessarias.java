package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Peca;

public class PecasNecessarias {
    private final Peca peca;
    private final int quantidade;

    public PecasNecessarias(Peca peca, int quantidade) {
        if (peca == null || quantidade <= 0) {
            throw new IllegalArgumentException("Peça e quantidade positiva são obrigatórias");
        }
        this.peca = peca;
        this.quantidade = quantidade;
    }

    public Peca getPeca() { return peca; }
    public int getQuantidade() { return quantidade; }
    public double getValorTotal() { return peca.getValor() * quantidade; }
}
