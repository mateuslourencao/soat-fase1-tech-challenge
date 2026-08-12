package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "clientes")
public class ClienteEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String telefone;

    protected ClienteEntity() {}

    public ClienteEntity(UUID id, String nome, String email, String documento, String telefone) {
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
