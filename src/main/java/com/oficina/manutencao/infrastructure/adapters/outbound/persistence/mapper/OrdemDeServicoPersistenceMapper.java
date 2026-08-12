package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.PecaNecessariaEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoServicosEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrdemDeServicoPersistenceMapper {

    public OrdemDeServicoEntity toEntity(OrdemDeServico ordem) {
        OrdemDeServicoEntity entity = new OrdemDeServicoEntity();

        entity.setId(ordem.getId());
        entity.setIdCliente(ordem.getIdCliente());
        entity.setIdVeiculo(ordem.getIdVeiculo());
        entity.setOrcamento(BigDecimal.valueOf(ordem.getOrcamento()));
        entity.setStatus(ordem.getStatus());
        entity.setDataCriacao(ordem.getDataCriacao());
        entity.setDataAtualizacao(ordem.getDataAtualizacao());
        entity.setDescricaoQueixas(ordem.getDescricaoQueixas());
        entity.setDiagnosticos(ordem.getDiagnosticos());

        entity.setPecasNecessarias(
                ordem.getPecasNecessarias().stream()
                        .map(peca -> toPecaEntity(ordem.getId(), peca))
                        .toList()
        );

        entity.setServicos(
                ordem.getServicos().stream()
                        .map(servico -> toServicoEntity(ordem.getId(), servico))
                        .toList()
        );

        return entity;
    }

    public OrdemDeServico toDomain(OrdemDeServicoEntity entity) {
        List<PecasNecessarias> pecas = entity.getPecasNecessarias().stream()
                .map(this::toPecaDomain)
                .toList();

        List<Servico> servicos = entity.getServicos().stream()
                .map(this::toServicoDomain)
                .toList();

        return new OrdemDeServico(
                entity.getId(),
                entity.getIdCliente(),
                entity.getIdVeiculo(),
                servicos,
                pecas,
                entity.getOrcamento().doubleValue(),
                entity.getStatus(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao(),
                entity.getDescricaoQueixas(),
                entity.getDiagnosticos()
        );
    }

    private PecaNecessariaEntity toPecaEntity(
            java.util.UUID ordemId,
            PecasNecessarias peca
    ) {
        return new PecaNecessariaEntity(
                ordemId,
                peca.getPeca().getId(),
                peca.getQuantidade(),
                BigDecimal.valueOf(peca.getValorUnitario())
        );
    }

    private PecasNecessarias toPecaDomain(PecaNecessariaEntity entity) {
        // A forma exata depende de como você modelar PecaEntity / PecaJpaRepository.
        // A ideia é reconstruir a peça e criar PecasNecessarias com seus dados.
        throw new UnsupportedOperationException("Implementar conforme PecaEntity");
    }

    private OrdemDeServicoServicosEntity toServicoEntity(
            java.util.UUID ordemId,
            Servico servico
    ) {
        return new OrdemDeServicoServicosEntity(
                ordemId,
                servico.getId(),
                BigDecimal.valueOf(servico.getValor())
        );
    }

    private Servico toServicoDomain(OrdemDeServicoServicosEntity entity) {
        // Precisa obter descrição do serviço ou mantê-la no mapeamento via relação com ServicoEntity.
        throw new UnsupportedOperationException("Implementar conforme ServicoEntity");
    }
}