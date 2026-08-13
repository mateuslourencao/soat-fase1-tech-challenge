package com.oficina.administrativo.domain.model;

public class Funcionario {

    private final int id;
    private final String nome;
    private final String email;
    private final String senhaHash;
    private final PerfilFuncionario perfil;
    private final boolean ativo;

    public Funcionario(int id, String nome, String email, String senhaHash, PerfilFuncionario perfil, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public PerfilFuncionario getPerfil() { return perfil; }
    public boolean isAtivo() { return ativo; }
}
