package com.oficina.estoque.domain.model;

import java.util.UUID;

public class Servico {

    private int id;
    private String descricao;
    private double valor;

    public Servico(String descricao, double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public Servico(int id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
}
