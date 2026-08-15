package com.oficina.administrativo.infrastructure.adapters.outbound.persistence.entity;

import com.oficina.administrativo.domain.model.PerfilFuncionario;
import jakarta.persistence.*;

@Entity
@Table(name = "funcionarios")
public class FuncionarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilFuncionario perfil;

    @Column(nullable = false)
    private boolean ativo;

    protected FuncionarioEntity() { }

    public FuncionarioEntity(String nome, String email, String senhaHash, PerfilFuncionario perfil, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public FuncionarioEntity(int id, String nome, String email, String senhaHash, PerfilFuncionario perfil, boolean ativo) {
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
