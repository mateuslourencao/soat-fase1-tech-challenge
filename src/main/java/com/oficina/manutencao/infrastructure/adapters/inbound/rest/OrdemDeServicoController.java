package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.CadastrarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.CriarOrdemDeServicoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ItensOSRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.OrdemDeServicoResponseDTO;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ordensdeservico")
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
    public ResponseEntity<OrdemDeServicoResponseDTO> criar(@Valid @RequestBody @NonNull CriarOrdemDeServicoRequestDTO request) {
        OrdemDeServico salvo = cadastrarOrdemDeServico.cadastrarOrdemDeServico(
                new OrdemDeServico(request.documentoCliente(), request.placaVeiculo(), request.descricaoQueixas())
        );
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServicoResponseDTO>> listar() {
        List<OrdemDeServicoResponseDTO> lista = listarOrdensDeServico.listarOrdensDeServico().stream()
                .map(OrdemDeServicoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServicoResponseDTO> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(buscarOrdemDeServico.buscarOrdemDeServico(id)));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<OrdemDeServicoResponseDTO> atualizarItens(@PathVariable int id, @Valid @RequestBody ItensOSRequestDTO request) {
        List<AtualizarItensOrdemDeServicoUseCase.PecaItemInput> pecas = request.pecasNecessarias() == null ? List.of() : request.pecasNecessarias().stream()
                .map(p -> new AtualizarItensOrdemDeServicoUseCase.PecaItemInput(p.pecaId(), p.quantidade()))
                .toList();

        OrdemDeServico response = atualizarItensOrdemDeServico.atualizarItensOrdemDeServico(id, pecas, request.servicosIds());
        return ResponseEntity.ok(new OrdemDeServicoResponseDTO(response));
    }
}
