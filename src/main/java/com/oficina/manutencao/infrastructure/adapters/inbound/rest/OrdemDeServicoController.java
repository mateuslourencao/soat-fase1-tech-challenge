package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.CadastrarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.CriarOrdemDeServicoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ItensOSRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.OrdemDeServicoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ordensdeservico")
@Tag(name = "Ordens de Serviço", description = "Gestão de aberturas e acompanhamento de ordens de serviço")
class OrdemDeServicoController {

    private final CadastrarOrdemDeServicoUseCase cadastrarOrdemDeServico;
    private final ListarOrdensDeServicoUseCase listarOrdensDeServico;
    private final BuscarOrdemDeServicoUseCase buscarOrdemDeServico;
    private final AtualizarItensOrdemDeServicoUseCase atualizarItensOrdemDeServico;

    OrdemDeServicoController(CadastrarOrdemDeServicoUseCase cadastrarOrdemDeServico,
                             ListarOrdensDeServicoUseCase listarOrdensDeServico,
                             BuscarOrdemDeServicoUseCase buscarOrdemDeServico,
                             AtualizarItensOrdemDeServicoUseCase atualizarItensOrdemDeServico) {
        this.cadastrarOrdemDeServico = cadastrarOrdemDeServico;
        this.listarOrdensDeServico = listarOrdensDeServico;
        this.buscarOrdemDeServico = buscarOrdemDeServico;
        this.atualizarItensOrdemDeServico = atualizarItensOrdemDeServico;
    }

    @PostMapping
    @Operation(summary = "Criar ordem de serviço", description = "Abre uma nova ordem de serviço para um cliente e veículo")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço criada com sucesso")
    public ResponseEntity<OrdemDeServicoResponseDTO> criar(@Valid @RequestBody @NonNull CriarOrdemDeServicoRequestDTO request) {
        OrdemDeServico salvo = cadastrarOrdemDeServico.cadastrarOrdemDeServico(
                new OrdemDeServico(request.documentoCliente(), request.placaVeiculo(), request.descricaoQueixas())
        );
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(salvo));
    }

    @GetMapping
    @Operation(summary = "Listar ordens de serviço", description = "Retorna uma lista de todas as ordens de serviço cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de ordens de serviço retornada com sucesso")
    public ResponseEntity<List<OrdemDeServicoResponseDTO>> listar() {
        List<OrdemDeServicoResponseDTO> lista = listarOrdensDeServico.listarOrdensDeServico().stream()
                .map(OrdemDeServicoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ordem de serviço por ID", description = "Retorna os detalhes de uma ordem de serviço específica")
    @ApiResponse(responseCode = "200", description = "Ordem de serviço encontrada")
    @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada")
    public ResponseEntity<OrdemDeServicoResponseDTO> buscarPorId(@Parameter(description = "ID da ordem de serviço") @PathVariable int id) {
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(buscarOrdemDeServico.buscarOrdemDeServico(id)));
    }

    @PostMapping("/{id}/itens")
    @Operation(summary = "Atualizar itens da ordem de serviço", description = "Adiciona peças e serviços a uma ordem de serviço aberta")
    @ApiResponse(responseCode = "200", description = "Itens atualizados com sucesso")
    public ResponseEntity<OrdemDeServicoResponseDTO> atualizarItens(@Parameter(description = "ID da ordem de serviço") @PathVariable int id, @Valid @RequestBody ItensOSRequestDTO request) {
        List<AtualizarItensOrdemDeServicoUseCase.PecaItemInput> pecas = request.pecasNecessarias() == null ? List.of() : request.pecasNecessarias().stream()
                .map(p -> new AtualizarItensOrdemDeServicoUseCase.PecaItemInput(p.pecaId(), p.quantidade()))
                .toList();

        OrdemDeServico response = atualizarItensOrdemDeServico.atualizarItensOrdemDeServico(id, pecas, request.servicosIds());
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(response));
    }
}
