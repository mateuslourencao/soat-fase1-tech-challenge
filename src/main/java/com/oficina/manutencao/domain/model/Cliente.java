package com.oficina.manutencao.domain.model;

public class Cliente {

    private final String documento;
    private final String nome;
    private final String email;
    private final String telefone;

    public Cliente(String documento, String nome, String email, String telefone) {
        this.documento = documento;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getDocumento() { return documento; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}
