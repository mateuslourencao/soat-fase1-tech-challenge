package com.oficina.estoque.domain.model;

public class Servico {

    private int id;
    private final String descricao;
    private final double valor;

    public Servico(String descricao, Double valor) {
        this(descricao, validarValorObrigatorio(valor));
    }

    public Servico(String descricao, double valor) {
        validarDados(descricao, valor);
        this.descricao = descricao;
        this.valor = valor;
    }

    public Servico(int id, String descricao, double valor) {
        validarDados(descricao, valor);
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }

    private static void validarDados(String descricao, double valor) {
        if (descricao == null || descricao.isBlank() || valor < 0) {
            throw new IllegalArgumentException("Dados do serviço inválidos");
        }
    }

    private static double validarValorObrigatorio(Double valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Dados do serviço inválidos");
        }
        return valor;
    }

    public int getId() { return id; }
    public String getDescricao() { return descricao; }
    public double getValor() { return valor; }
}
