package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ordens_de_servico_servicos")
public class OrdemDeServicoServicosEntity {

    @EmbeddedId
    private OrdemDeServicoServicosId id; // ordem_de_servico_id + servico_id

    @MapsId("ordemDeServicoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_de_servico_id")
    private OrdemDeServicoEntity ordemDeServico;

    @Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCobrado;

    protected OrdemDeServicoServicosEntity() {}

    public OrdemDeServicoServicosEntity(
            UUID ordemDeServicoId,
            UUID servicoId,
            BigDecimal valorCobrado
    ) {
        this.id = new OrdemDeServicoServicosId(ordemDeServicoId, servicoId);
        this.valorCobrado = valorCobrado;
    }

    public void setOrdemDeServico(OrdemDeServicoEntity ordemDeServico) {
        this.ordemDeServico = ordemDeServico;
    }
}
