package com.oficina.estoque.domain.model;

import java.util.UUID;

public class Servico {

    private UUID id;
    private String descricao;
    private double valor;

    public Servico(UUID id, String descricao, double valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public UUID getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
}
