package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pecas_necessarias")
public class PecaNecessariaEntity {

    @EmbeddedId
    private PecaNecessariaId id; // ordem_de_servico_id + peca_id

    @MapsId("ordemDeServicoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_de_servico_id")
    private OrdemDeServicoEntity ordemDeServico;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    protected PecaNecessariaEntity() {}

    public PecaNecessariaEntity(
            UUID ordemDeServicoId,
            UUID pecaId,
            Integer quantidade,
            BigDecimal valorUnitario
    ) {
        this.id = new PecaNecessariaId(ordemDeServicoId, pecaId);
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public void setOrdemDeServico(OrdemDeServicoEntity ordemDeServico) {
        this.ordemDeServico = ordemDeServico;
    }
}
