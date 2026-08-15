package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PecaNecessariaId implements Serializable {
    @Column(name = "ordem_de_servico_id")
    private int ordemDeServicoId;

    @Column(name = "peca_id")
    private int pecaId;

    protected PecaNecessariaId() {
        // Construtor padrão exigido pelo JPA
    }

    public PecaNecessariaId(int ordemDeServicoId, int pecaId) {
        this.ordemDeServicoId = ordemDeServicoId;
        this.pecaId = pecaId;
    }

    public int getOrdemDeServicoId() {
        return ordemDeServicoId;
    }

    public int getPecaId() {
        return pecaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PecaNecessariaId that = (PecaNecessariaId) o;
        return ordemDeServicoId == that.ordemDeServicoId && pecaId == that.pecaId;
    }

    @Override
    public int hashCode() {
        int result = ordemDeServicoId;
        result = 31 * result + pecaId;
        return result;
    }
}
