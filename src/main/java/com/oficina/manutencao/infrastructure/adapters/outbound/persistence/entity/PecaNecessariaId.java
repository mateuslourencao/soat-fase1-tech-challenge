package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class PecaNecessariaId implements Serializable {
    @Column(name = "ordem_de_servico_id")
    private UUID ordemDeServicoId;

    @Column(name = "peca_id")
    private UUID pecaId;

    protected PecaNecessariaId() {
    }

    public PecaNecessariaId(UUID ordemDeServicoId, UUID pecaId) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.pecaId = pecaId;
    }
}
