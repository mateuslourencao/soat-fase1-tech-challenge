package com.oficina.manutencao.domain.model;

import java.util.UUID;

public class Cliente {

    private String documento;
    private String nome;
    private String email;
    private String telefone;

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
