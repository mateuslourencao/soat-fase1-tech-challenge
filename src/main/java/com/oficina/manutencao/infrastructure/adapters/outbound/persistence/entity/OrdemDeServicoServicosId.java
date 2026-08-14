package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class OrdemDeServicoServicosId implements Serializable {
    @Column(name = "ordem_de_servico_id")
    private int ordemDeServicoId;

    @Column(name = "servico_id")
    private int servicoId;

    protected OrdemDeServicoServicosId() {
    }

    public OrdemDeServicoServicosId(int ordemDeServicoId, int servicoId) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.servicoId = servicoId;
    }

    public int getOrdemDeServicoId() {
        return ordemDeServicoId;
    }

    public int getServicoId() {
        return servicoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdemDeServicoServicosId that = (OrdemDeServicoServicosId) o;
        return ordemDeServicoId == that.ordemDeServicoId && servicoId == that.servicoId;
    }

    @Override
    public int hashCode() {
        int result = ordemDeServicoId;
        result = 31 * result + servicoId;
        return result;
    }
}
