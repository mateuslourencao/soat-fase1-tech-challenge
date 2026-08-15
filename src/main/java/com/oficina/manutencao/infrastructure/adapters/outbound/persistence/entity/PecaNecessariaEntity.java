package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pecas_necessarias")
public class PecaNecessariaEntity {

    @EmbeddedId
    private PecaNecessariaId id; // ordem_de_servico_id + peca_id

    @MapsId("ordemDeServicoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_de_servico_id")
    private OrdemDeServicoEntity ordemDeServico;

    @MapsId("pecaId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "peca_id")
    private PecaEntity peca;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "valor_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    protected PecaNecessariaEntity() {}

    public PecaNecessariaEntity(
            int ordemDeServicoId,
            PecaEntity peca,
            Integer quantidade,
            BigDecimal valorUnitario
    ) {
        this.id = new PecaNecessariaId(ordemDeServicoId, peca.getId());
        this.peca = peca;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public void setOrdemDeServico(OrdemDeServicoEntity ordemDeServico) {
        this.ordemDeServico = ordemDeServico;
    }

    public PecaEntity getPeca() { return peca; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getValorUnitario() { return valorUnitario; }
}
