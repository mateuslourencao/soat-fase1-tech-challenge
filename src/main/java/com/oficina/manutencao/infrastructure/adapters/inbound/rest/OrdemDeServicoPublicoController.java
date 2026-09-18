package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.AprovarOrcamentoClienteUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.AprovarOrcamentoPublicoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.OrdemDeServicoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/publico/ordensdeservico")
@Tag(name = "Ordens de Serviço Públicas", description = "Consulta e aprovação pública de orçamento pelo cliente")
class OrdemDeServicoPublicoController {

    private final BuscarOrdemDeServicoUseCase buscarOrdemDeServico;
    private final AprovarOrcamentoClienteUseCase aprovarOrcamentoCliente;

    OrdemDeServicoPublicoController(BuscarOrdemDeServicoUseCase buscarOrdemDeServico,
                                    AprovarOrcamentoClienteUseCase aprovarOrcamentoCliente) {
        this.buscarOrdemDeServico = buscarOrdemDeServico;
        this.aprovarOrcamentoCliente = aprovarOrcamentoCliente;
    }

    @PostMapping("/{id}/aprovar-orcamento")
    @Operation(summary = "Aprovar orçamento", description = "Aprova ou rejeita o orçamento informando o documento do cliente")
    public ResponseEntity<Void> aprovarOrcamento(@PathVariable int id,
                                                  @Valid @RequestBody AprovarOrcamentoPublicoRequestDTO request) {
        aprovarOrcamentoCliente.aprovarOrcamento(id, request.documentoCliente(), request.aprovado());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar ordem de serviço publicamente")
    public ResponseEntity<OrdemDeServicoResponseDTO> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(buscarOrdemDeServico.buscarOrdemDeServico(id)));
    }
}
