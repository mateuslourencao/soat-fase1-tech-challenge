package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoServicosEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.PecaNecessariaEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrdemDeServicoPersistenceMapper {
    private final PecaJpaRepository pecaRepository;
    private final ServicoJpaRepository servicoRepository;

    public OrdemDeServicoPersistenceMapper(PecaJpaRepository pecaRepository, ServicoJpaRepository servicoRepository) {
        this.pecaRepository = pecaRepository;
        this.servicoRepository = servicoRepository;
    }

    public OrdemDeServicoEntity toEntity(OrdemDeServico ordem) {
        OrdemDeServicoEntity entity = new OrdemDeServicoEntity();
        entity.setId(ordem.getId());
        entity.setDocumentoCliente(ordem.getDocumentoCliente());
        entity.setPlacaVeiculo(ordem.getPlacaVeiculo());
        entity.setOrcamento(BigDecimal.valueOf(ordem.getOrcamento()));
        entity.setStatus(ordem.getStatus());
        entity.setDataCriacao(ordem.getDataCriacao());
        entity.setDataAtualizacao(ordem.getDataAtualizacao());
        entity.setDescricaoQueixas(ordem.getDescricaoQueixas());
        entity.setDiagnosticos(ordem.getDiagnosticos());
        entity.setPecasNecessarias(ordem.getPecasNecessarias().stream()
                .map(peca -> toPecaEntity(ordem.getId(), peca))
                .toList());
        entity.setServicos(ordem.getServicos().stream()
                .map(servico -> toServicoEntity(ordem.getId(), servico))
                .toList());
        return entity;
    }

    public OrdemDeServico toDomain(OrdemDeServicoEntity entity) {
        List<PecasNecessarias> pecas = entity.getPecasNecessarias().stream().map(this::toPecaDomain).toList();
        List<Servico> servicos = entity.getServicos().stream().map(this::toServicoDomain).toList();
        return new OrdemDeServico(entity.getId(), entity.getDocumentoCliente(), entity.getPlacaVeiculo(), servicos, pecas,
                entity.getOrcamento() == null ? 0D : entity.getOrcamento().doubleValue(), entity.getStatus(),
                entity.getDataCriacao(), entity.getDataAtualizacao(), entity.getDescricaoQueixas(), entity.getDiagnosticos());
    }

    private PecaNecessariaEntity toPecaEntity(int ordemId, PecasNecessarias peca) {
        return new PecaNecessariaEntity(ordemId, pecaRepository.getReferenceById(peca.peca().getId()),
                peca.quantidade(), BigDecimal.valueOf(peca.getValorUnitario()));
    }

    private PecasNecessarias toPecaDomain(PecaNecessariaEntity entity) {
        PecaEntity peca = entity.getPeca();
        Peca pecaDomain = new Peca(peca.getId(), peca.getDescricao(), entity.getValorUnitario().doubleValue(), peca.getQuantidade());
        return new PecasNecessarias(pecaDomain, entity.getQuantidade());
    }

    private OrdemDeServicoServicosEntity toServicoEntity(int ordemId, Servico servico) {
        return new OrdemDeServicoServicosEntity(ordemId, servicoRepository.getReferenceById(servico.getId()),
                BigDecimal.valueOf(servico.getValor()));
    }

    private Servico toServicoDomain(OrdemDeServicoServicosEntity entity) {
        ServicoEntity servico = entity.getServico();
        return new Servico(servico.getId(), servico.getDescricao(), entity.getValorCobrado().doubleValue());
    }
}
