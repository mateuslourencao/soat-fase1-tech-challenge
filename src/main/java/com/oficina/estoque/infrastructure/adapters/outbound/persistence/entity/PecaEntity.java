package com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pecas")
public class PecaEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private int quantidade;

    protected PecaEntity() {}

    public PecaEntity(UUID id, String descricao, BigDecimal valor, int quantidade) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public UUID getId() { return id; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public int getQuantidade() { return quantidade; }
}
