package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity;

import com.oficina.manutencao.domain.model.StatusOS;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ordens_de_servico")
public class OrdemDeServicoEntity {

    @Id
    @Column(name = "id", length = 36)
    private UUID id;

    @Column(name = "documento_cliente", nullable = false, length = 255)
    private String documentoCliente;

    @Column(name = "placa_veiculo", nullable = false, length = 255)
    private String placaVeiculo;

    @Column(name = "orcamento", precision = 10, scale = 2)
    private BigDecimal orcamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private StatusOS status;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "descricao_queixas")
    private String descricaoQueixas;

    @Column(name = "diagnosticos")
    private String diagnosticos;

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PecaNecessariaEntity> pecasNecessarias = new ArrayList<>();

    @OneToMany(mappedBy = "ordemDeServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemDeServicoServicosEntity> servicos = new ArrayList<>();

    public OrdemDeServicoEntity() {}

    public UUID getId() { return id; }
    public String getDocumentoCliente() { return documentoCliente; }
    public String getPlacaVeiculo() { return placaVeiculo; }
    public BigDecimal getOrcamento() { return orcamento; }
    public StatusOS getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public String getDescricaoQueixas() { return descricaoQueixas; }
    public String getDiagnosticos() { return diagnosticos; }
    public List<PecaNecessariaEntity> getPecasNecessarias() { return pecasNecessarias; }
    public List<OrdemDeServicoServicosEntity> getServicos() { return servicos; }

    public void setId(UUID id) { this.id = id; }
    public void setDocumentoCliente(String documentoCliente) { this.documentoCliente = documentoCliente; }
    public void setPlacaVeiculo(String placaVeiculo) { this.placaVeiculo = placaVeiculo; }
    public void setOrcamento(BigDecimal orcamento) { this.orcamento = orcamento; }
    public void setStatus(StatusOS status) { this.status = status; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public void setDescricaoQueixas(String descricaoQueixas) { this.descricaoQueixas = descricaoQueixas; }
    public void setDiagnosticos(String diagnosticos) { this.diagnosticos = diagnosticos; }

    public void setPecasNecessarias(List<PecaNecessariaEntity> pecasNecessarias) {
        this.pecasNecessarias.clear();
        pecasNecessarias.forEach(peca -> {
            peca.setOrdemDeServico(this);
            this.pecasNecessarias.add(peca);
        });
    }

    public void setServicos(List<OrdemDeServicoServicosEntity> servicos) {
        this.servicos.clear();
        servicos.forEach(servico -> {
            servico.setOrdemDeServico(this);
            this.servicos.add(servico);
        });
    }
}
