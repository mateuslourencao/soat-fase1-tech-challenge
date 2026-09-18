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
        this.documentoCliente = textoObrigatorio(documentoCliente, "Documento do cliente");
        this.placaVeiculo = textoObrigatorio(placaVeiculo, "Placa do veículo");
        this.descricaoQueixas = textoObrigatorio(descricaoQueixas, "Descrição das queixas");
        this.servicos = List.of();
        this.pecasNecessarias = List.of();
        this.status = StatusOS.RECEBIDA;
        this.dataCriacao = LocalDateTime.now(ZoneId.of("UTC"));
        this.dataAtualizacao = this.dataCriacao;
    }

    public OrdemDeServico(String documentoCliente, String placaVeiculo, String descricaoQueixas) {
        this.id = 0;
        this.documentoCliente = textoObrigatorio(documentoCliente, "Documento do cliente");
        this.placaVeiculo = textoObrigatorio(placaVeiculo, "Placa do veículo");
        this.descricaoQueixas = textoObrigatorio(descricaoQueixas, "Descrição das queixas");
        this.servicos = List.of();
        this.pecasNecessarias = List.of();
        this.status = StatusOS.RECEBIDA;
        this.dataCriacao = LocalDateTime.now(ZoneId.of("UTC"));
        this.dataAtualizacao = this.dataCriacao;
    }

    private OrdemDeServico(Builder builder) {
        this.id = builder.id;
        this.documentoCliente = textoObrigatorio(builder.documentoCliente, "Documento do cliente");
        this.placaVeiculo = textoObrigatorio(builder.placaVeiculo, "Placa do veículo");
        this.servicos = List.copyOf(builder.servicos == null ? List.of() : builder.servicos);
        this.pecasNecessarias = List.copyOf(builder.pecasNecessarias == null ? List.of() : builder.pecasNecessarias);
        if (builder.orcamento < 0 || builder.status == null || builder.dataCriacao == null) {
            throw new IllegalArgumentException("Ordem de serviço inválida");
        }
        this.orcamento = builder.orcamento;
        this.status = builder.status;
        this.dataCriacao = builder.dataCriacao;
        this.dataAtualizacao = builder.dataAtualizacao == null ? builder.dataCriacao : builder.dataAtualizacao;
        this.descricaoQueixas = textoObrigatorio(builder.descricaoQueixas, "Descrição das queixas");
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
        List<PecasNecessarias> pecas = pecasNecessarias == null ? List.of() : List.copyOf(pecasNecessarias);
        List<Servico> servicosAtualizados = servicos == null ? List.of() : List.copyOf(servicos);
        validarPodeAtualizarItens();
        if (pecas.isEmpty() && servicosAtualizados.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos uma peça necessária ou um serviço");
        }
        this.pecasNecessarias = pecas;
        this.servicos = servicosAtualizados;
        this.orcamento = pecas.stream().mapToDouble(PecasNecessarias::getValorTotal).sum()
                + servicosAtualizados.stream().mapToDouble(Servico::getValor).sum();
        registraAtualizacao();
    }

    public void transicionarPara(StatusOS destino) {
        if (!transicaoPermitida(status, destino)) {
            throw new IllegalStateException("Transição inválida: status atual " + status);
        }
        this.status = destino;
        registraAtualizacao();
    }

    public void validarPodeAtualizarItens() {
        if (status != StatusOS.EM_DIAGNOSTICO) {
            throw new IllegalStateException("Itens só podem ser atualizados durante o diagnóstico");
        }
    }

    private static boolean transicaoPermitida(StatusOS origem, StatusOS destino) {
        return (origem == StatusOS.RECEBIDA && destino == StatusOS.EM_DIAGNOSTICO)
                || (origem == StatusOS.EM_DIAGNOSTICO && destino == StatusOS.AGUARDANDO_APROVACAO)
                || (origem == StatusOS.AGUARDANDO_APROVACAO && destino == StatusOS.EM_EXECUCAO)
                || (origem == StatusOS.AGUARDANDO_APROVACAO && destino == StatusOS.FINALIZADA)
                || (origem == StatusOS.EM_EXECUCAO && destino == StatusOS.FINALIZADA)
                || (origem == StatusOS.FINALIZADA && destino == StatusOS.ENTREGUE);
    }

    private static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " é obrigatório");
        }
        return valor.trim();
    }

    private void registraAtualizacao() { this.dataAtualizacao = LocalDateTime.now(ZoneId.of("UTC")); }
}
