package com.oficina.manutencao.domain.model;

public class Veiculo {

    private final String placa;
    private final String marca;
    private final String modelo;
    private final int ano;

    public Veiculo(String placa, String marca, String modelo, int ano) {
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
