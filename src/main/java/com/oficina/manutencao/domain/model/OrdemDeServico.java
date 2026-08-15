package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Servico;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
        this.dataCriacao = LocalDateTime.now(ZoneId.of("UTC"));
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
        this.dataCriacao = LocalDateTime.now(ZoneId.of("UTC"));
        this.dataAtualizacao = this.dataCriacao;
    }

    private OrdemDeServico(Builder builder) {
        this.id = builder.id;
        this.documentoCliente = builder.documentoCliente;
        this.placaVeiculo = builder.placaVeiculo;
        this.servicos = builder.servicos;
        this.pecasNecessarias = builder.pecasNecessarias;
        this.orcamento = builder.orcamento;
        this.status = builder.status;
        this.dataCriacao = builder.dataCriacao;
        this.dataAtualizacao = builder.dataAtualizacao;
        this.descricaoQueixas = builder.descricaoQueixas;
        this.diagnosticos = builder.diagnosticos;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String documentoCliente;
        private String placaVeiculo;
        private List<Servico> servicos = List.of();
        private List<PecasNecessarias> pecasNecessarias = List.of();
        private double orcamento;
        private StatusOS status = StatusOS.RECEBIDA;
        private LocalDateTime dataCriacao = LocalDateTime.now(ZoneId.of("UTC"));
        private LocalDateTime dataAtualizacao;
        private String descricaoQueixas;
        private String diagnosticos;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder documentoCliente(String documentoCliente) {
            this.documentoCliente = documentoCliente;
            return this;
        }

        public Builder placaVeiculo(String placaVeiculo) {
            this.placaVeiculo = placaVeiculo;
            return this;
        }

        public Builder servicos(List<Servico> servicos) {
            this.servicos = servicos;
            return this;
        }

        public Builder pecasNecessarias(List<PecasNecessarias> pecasNecessarias) {
            this.pecasNecessarias = pecasNecessarias;
            return this;
        }

        public Builder orcamento(double orcamento) {
            this.orcamento = orcamento;
            return this;
        }

        public Builder status(StatusOS status) {
            this.status = status;
            return this;
        }

        public Builder dataCriacao(LocalDateTime dataCriacao) {
            this.dataCriacao = dataCriacao;
            return this;
        }

        public Builder dataAtualizacao(LocalDateTime dataAtualizacao) {
            this.dataAtualizacao = dataAtualizacao;
            return this;
        }

        public Builder descricaoQueixas(String descricaoQueixas) {
            this.descricaoQueixas = descricaoQueixas;
            return this;
        }

        public Builder diagnosticos(String diagnosticos) {
            this.diagnosticos = diagnosticos;
            return this;
        }

        public OrdemDeServico build() {
            return new OrdemDeServico(this);
        }
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

    private void registraAtualizacao() { this.dataAtualizacao = LocalDateTime.now(ZoneId.of("UTC")); }
}
