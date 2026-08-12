package com.oficina.manutencao.domain.model;

import java.util.UUID;

public class Cliente {

    private UUID id;
    private String nome;
    private String email;
    private String documento;
    private String telefone;

    public Cliente(UUID id, String nome, String email, String documento, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.telefone = telefone;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getDocumento() { return documento; }
    public String getTelefone() { return telefone; }
}
