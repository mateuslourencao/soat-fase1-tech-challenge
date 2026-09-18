package com.oficina.estoque.domain.model;

public class Peca {

    private int id;
    private final String descricao;
    private final double valor;
    private int quantidade;

    public Peca(String descricao, double valor, int quantidade) {
        this.descricao = descricao;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public Peca(int id, String descricao, double valor, int quantidade) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
    public int getQuantidade() { return quantidade; }

    public void baixarEstoque(int quantidade) {
        validarQuantidadeParaBaixa(quantidade);
        if (this.quantidade < quantidade) {
            throw new IllegalStateException("Estoque insuficiente");
        }
        this.quantidade -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        validarQuantidadeParaReposicao(quantidade);
        this.quantidade += quantidade;
    }

    public static void validarQuantidadeParaBaixa(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade para baixa deve ser positiva");
        }
    }

    public static void validarQuantidadeParaReposicao(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade para repor deve ser positiva");
        }
    }
}
