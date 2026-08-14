package com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto;

import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoResponseDTO;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;

import java.time.LocalDateTime;
import java.util.List;

public record OrdemDeServicoResponseDTO(
        int id,
        String documentoCliente,
        String placaVeiculo,
        List<ServicoResponseDTO> servicos,
        List<PecaNecessariaResponseDTO> pecasNecessarias,
        double orcamento,
        StatusOS status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        String descricaoQueixas,
        String diagnosticos
) {
    public OrdemDeServicoResponseDTO(OrdemDeServico os) {
        this(
                os.getId(),
                os.getDocumentoCliente(),
                os.getPlacaVeiculo(),
                os.getServicos() != null ? os.getServicos().stream().map(ServicoResponseDTO::new).toList() : List.of(),
                os.getPecasNecessarias() != null ? os.getPecasNecessarias().stream().map(PecaNecessariaResponseDTO::new).toList() : List.of(),
                os.getOrcamento(),
                os.getStatus(),
                os.getDataCriacao(),
                os.getDataAtualizacao(),
                os.getDescricaoQueixas(),
                os.getDiagnosticos()
        );
    }
}
