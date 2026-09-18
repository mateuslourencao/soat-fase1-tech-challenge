package com.oficina.manutencao.domain.model;

public class Cliente {

    private final String documento;
    private final String nome;
    private final String email;
    private final String telefone;

    public Cliente(String documento, String nome, String email, String telefone) {
        this.documento = textoObrigatorio(documento, "Documento");
        this.nome = textoObrigatorio(nome, "Nome");
        this.email = textoObrigatorio(email, "E-mail").toLowerCase();
        this.telefone = textoObrigatorio(telefone, "Telefone");
    }

    public Cliente atualizarDados(String nome, String email, String telefone) {
        return new Cliente(documento, nome, email, telefone);
    }

    private static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }
        return valor.trim();
    }

    public String getDocumento() { return documento; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}
