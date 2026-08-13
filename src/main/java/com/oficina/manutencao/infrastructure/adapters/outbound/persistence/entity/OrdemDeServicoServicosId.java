package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class OrdemDeServicoServicosId implements Serializable {
    @Column(name = "ordem_de_servico_id")
    private UUID ordemDeServicoId;

    @Column(name = "servico_id")
    private int servicoId;

    protected OrdemDeServicoServicosId() {
    }

    public OrdemDeServicoServicosId(UUID ordemDeServicoId, int servicoId) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.servicoId = servicoId;
    }

}
