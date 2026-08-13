package com.oficina.manutencao.application.service;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;

import java.util.List;

public class AtualizarItensOrdemDeServicoService implements AtualizarItensOrdemDeServicoUseCase {
    private final OrdemDeServicoRepositoryPort repositorio;
    public AtualizarItensOrdemDeServicoService(OrdemDeServicoRepositoryPort repositorio) { this.repositorio = repositorio; }

    public OrdemDeServico atualizarItensOrdemDeServico(int id, List<PecasNecessarias> pecasNecessarias, List<Servico> servicos) {
        List<PecasNecessarias> pecas = pecasNecessarias == null ? List.of() : pecasNecessarias;
        List<Servico> servicosOrcados = servicos == null ? List.of() : servicos;
        if (pecas.isEmpty() && servicosOrcados.isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos uma peça necessária ou um serviço");
        }
        OrdemDeServico ordem = repositorio.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Ordem de serviço não encontrada"));
        if (ordem.getStatus() != StatusOS.EM_DIAGNOSTICO) throw new IllegalStateException("Itens só podem ser atualizados durante o diagnóstico");
        ordem.registrarAtualizacaoDeItens(pecas, servicosOrcados);
        return repositorio.salvar(ordem);
    }
}
