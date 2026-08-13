package com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "servicos")
public class ServicoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    protected ServicoEntity() {}

    public ServicoEntity(int id, String descricao, BigDecimal valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
}
