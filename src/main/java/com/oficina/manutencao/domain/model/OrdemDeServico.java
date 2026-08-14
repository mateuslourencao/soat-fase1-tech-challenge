package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Servico;

import java.time.LocalDateTime;
import java.util.List;

public class OrdemDeServico {

    private final int id;
    private final String documentoCliente;
    private final String placaVeiculo;
    private List<Servico> servicos;
    private List<PecasNecessarias> pecasNecessarias;
    private double orcamento;
    private StatusOS status;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private final String descricaoQueixas;
    private String diagnosticos;

    public OrdemDeServico(int id, String documentoCliente, String placaVeiculo, String descricaoQueixas) {
        this.id = id;
        this.documentoCliente = documentoCliente;
        this.placaVeiculo = placaVeiculo;
        this.descricaoQueixas = descricaoQueixas;
        this.servicos = List.of();
        this.pecasNecessarias = List.of();
        this.status = StatusOS.RECEBIDA;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = this.dataCriacao;
    }

    public OrdemDeServico(String documentoCliente, String placaVeiculo, String descricaoQueixas) {
        this.id = 0;
        this.documentoCliente = documentoCliente;
        this.placaVeiculo = placaVeiculo;
        this.descricaoQueixas = descricaoQueixas;
        this.servicos = List.of();
        this.pecasNecessarias = List.of();
        this.status = StatusOS.RECEBIDA;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = this.dataCriacao;
    }

    public OrdemDeServico(int id, String documentoCliente, String placaVeiculo, List<Servico> servicos, List<PecasNecessarias> pecas,
                          double orcamento, StatusOS status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao,
                          String descricaoQueixas, String diagnosticos) {
        this.id = id;
        this.documentoCliente = documentoCliente;
        this.placaVeiculo = placaVeiculo;
        this.servicos = servicos;
        this.pecasNecessarias = pecas;
        this.orcamento = orcamento;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.descricaoQueixas = descricaoQueixas;
        this.diagnosticos = diagnosticos;
    }

    public int getId() { return id; }
    public String getDocumentoCliente() { return documentoCliente; }
    public String getPlacaVeiculo() { return placaVeiculo; }
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
