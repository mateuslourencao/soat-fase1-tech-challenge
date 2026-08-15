package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ordens_de_servico_servicos")
public class OrdemDeServicoServicosEntity {

    @EmbeddedId
    private OrdemDeServicoServicosId id; // ordem_de_servico_id + servico_id

    @MapsId("ordemDeServicoId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_de_servico_id")
    private OrdemDeServicoEntity ordemDeServico;

    @MapsId("servicoId")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "servico_id")
    private ServicoEntity servico;

    @Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCobrado;

    protected OrdemDeServicoServicosEntity() {
        // Construtor padrão exigido pelo JPA
    }

    public OrdemDeServicoServicosEntity(
            int ordemDeServicoId,
            ServicoEntity servico,
            BigDecimal valorCobrado
    ) {
        this.id = new OrdemDeServicoServicosId(ordemDeServicoId, servico.getId());
        this.servico = servico;
        this.valorCobrado = valorCobrado;
    }

    public void setOrdemDeServico(OrdemDeServicoEntity ordemDeServico) {
        this.ordemDeServico = ordemDeServico;
    }

    public ServicoEntity getServico() { return servico; }
    public BigDecimal getValorCobrado() { return valorCobrado; }
}
