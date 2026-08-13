package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "veiculos")
public class VeiculoEntity {
    @Id
    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int ano;

    protected VeiculoEntity() {}

    public VeiculoEntity(String placa, String marca, String modelo, int ano) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
    }

    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
}
