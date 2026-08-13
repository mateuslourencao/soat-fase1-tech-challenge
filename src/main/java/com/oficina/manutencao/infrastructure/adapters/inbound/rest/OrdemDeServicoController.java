package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoResponseDTO;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.AtualizarItensOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.BuscarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.CadastrarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdensDeServicoUseCase;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.ItensOSRequestDTO;
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
    public ResponseEntity<OrdemDeServico> criar(@Valid @RequestBody @NonNull OrdemDeServico request) {
        OrdemDeServico salvo = cadastrarOrdemDeServico.cadastrarOrdemDeServico(
                new OrdemDeServico(request.getDocumentoCliente(), request.getPlacaVeiculo(), request.getDescricaoQueixas())
        );
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listar() {
        return ResponseEntity.ok(listarOrdensDeServico.listarOrdensDeServico());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServico> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(buscarOrdemDeServico.buscarOrdemDeServico(id));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<OrdemDeServico> atualizarItens(@PathVariable int id, @Valid @RequestBody ItensOSRequestDTO request) {
        OrdemDeServico response = atualizarItensOrdemDeServico.atualizarItensOrdemDeServico(id, request.pecasNecessarias(), request.servicos());
        return ResponseEntity.ok(response);
    }
}
