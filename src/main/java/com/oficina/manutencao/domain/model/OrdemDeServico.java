package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Servico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrdemDeServico {

    private UUID id;
    private UUID idCliente;
    private UUID idVeiculo;
    private List<Servico> servicos;
    private List<PecasNecessarias> pecasNecessarias;
    private double orcamento;
    private StatusOS status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String descricaoQueixas;
    private String diagnosticos;

    public OrdemDeServico(UUID id, UUID idCliente, UUID idVeiculo, String descricaoQueixas) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVeiculo = idVeiculo;
        this.descricaoQueixas = descricaoQueixas;
        this.servicos = List.of();
        this.pecasNecessarias = List.of();
        this.status = StatusOS.RECEBIDA;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = this.dataCriacao;
    }

    public UUID getId() { return id; }
    public UUID getIdCliente() { return idCliente; }
    public UUID getIdVeiculo() { return idVeiculo; }
    public List<Servico> getServicos() { return servicos; }
    public List<PecasNecessarias> getPecasNecessarias() { return pecasNecessarias; }
    public double getOrcamento() { return orcamento; }
    public StatusOS getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public String getDescricaoQueixas() { return descricaoQueixas; }
    public String getDiagnosticos() { return diagnosticos; }

    public void registrarAtualizacaoDeItens(List<PecasNecessarias> pecasNecessarias, List<Servico> servicos) {
        this.pecasNecessarias = List.copyOf(pecasNecessarias);
        this.servicos = List.copyOf(servicos);
        this.orcamento = pecasNecessarias.stream().mapToDouble(PecasNecessarias::getValorTotal).sum()
                + servicos.stream().mapToDouble(Servico::getValor).sum();
        registraAtualizacao();
    }

    public void alterarStatus(StatusOS status) {
        this.status = status;
        registraAtualizacao();
    }

    private void registraAtualizacao() { this.dataAtualizacao = LocalDateTime.now(); }
}
