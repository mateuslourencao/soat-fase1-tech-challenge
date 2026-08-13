package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class PecaNecessariaId implements Serializable {
    @Column(name = "ordem_de_servico_id")
    private UUID ordemDeServicoId;

    @Column(name = "peca_id")
    private int pecaId;

    protected PecaNecessariaId() {
    }

    public PecaNecessariaId(UUID ordemDeServicoId, int pecaId) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.pecaId = pecaId;
    }
}
