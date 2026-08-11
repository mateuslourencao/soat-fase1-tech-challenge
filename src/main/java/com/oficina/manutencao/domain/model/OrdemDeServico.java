package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrdemDeServico {

    private UUID id;
    private UUID idCliente;
    private UUID idVeiculo;
    private List<Servico> servicos;
    private List<Peca> pecas;
    private double orcamento;
    private StatusOS status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String descricaoQueixas;
    private String diagnosticos;

}
