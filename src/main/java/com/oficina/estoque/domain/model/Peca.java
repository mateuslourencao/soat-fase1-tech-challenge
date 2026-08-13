package com.oficina.estoque.domain.model;

import java.util.UUID;

public class Peca {

    private int id;
    private String descricao;
    private double valor;
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

    public void atualizarQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
