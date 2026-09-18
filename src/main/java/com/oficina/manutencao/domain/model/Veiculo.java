package com.oficina.manutencao.domain.model;

public class Veiculo {

    private final String placa;
    private final String marca;
    private final String modelo;
    private final int ano;

    public Veiculo(String placa, String marca, String modelo, int ano) {
        this.placa = textoObrigatorio(placa, "Placa").toUpperCase();
        this.marca = textoObrigatorio(marca, "Marca");
        this.modelo = textoObrigatorio(modelo, "Modelo");
        if (ano <= 0) {
            throw new IllegalArgumentException("Ano deve ser positivo");
        }
        this.ano = ano;
    }

    public Veiculo atualizarDados(String marca, String modelo, int ano) {
        return new Veiculo(placa, marca, modelo, ano);
    }

    private static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }
        return valor.trim();
    }

    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAno() { return ano; }
}
