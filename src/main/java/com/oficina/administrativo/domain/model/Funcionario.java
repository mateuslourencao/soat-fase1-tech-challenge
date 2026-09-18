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
        this.nome = normalizarNome(nome);
        this.email = normalizarEmail(email);
        this.senhaHash = senhaHash;
        this.perfil = validarPerfil(perfil);
        this.ativo = ativo;
    }

    public Funcionario atualizarDados(String nome, String email, PerfilFuncionario perfil) {
        return new Funcionario(id, nome, email, senhaHash, perfil, ativo);
    }

    public Funcionario ativar() {
        return new Funcionario(id, nome, email, senhaHash, perfil, true);
    }

    public Funcionario inativar() {
        return new Funcionario(id, nome, email, senhaHash, perfil, false);
    }

    public boolean podeAutenticar() {
        return ativo;
    }

    private static String normalizarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        return nome.trim();
    }

    private static String normalizarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        return email.trim().toLowerCase();
    }

    private static PerfilFuncionario validarPerfil(PerfilFuncionario perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Dados do funcionário inválidos");
        }
        return perfil;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public PerfilFuncionario getPerfil() { return perfil; }
    public boolean isAtivo() { return ativo; }
}
