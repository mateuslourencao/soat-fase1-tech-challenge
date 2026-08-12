package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.CriarOrdemDeServicoUseCase;
import com.oficina.manutencao.domain.ports.inbound.ListarOrdemDeServicoUseCase;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ordemdeservico")
class OrdemDeServicoController {

    private final CriarOrdemDeServicoUseCase criarOrdemDeServico;
    private final ListarOrdemDeServicoUseCase listarOrdemDeServico;

    OrdemDeServicoController(CriarOrdemDeServicoUseCase criarOrdemDeServico, ListarOrdemDeServicoUseCase listarOrdemDeServico) {
        this.criarOrdemDeServico = criarOrdemDeServico;
        this.listarOrdemDeServico = listarOrdemDeServico;
    }

    @PostMapping
    public ResponseEntity<Void> criarOrdemDeServico(@Valid @RequestBody @NonNull OrdemDeServico request) {
        criarOrdemDeServico.criarOrdemDeServico(
                new OrdemDeServico(UUID.randomUUID(), request.getIdCliente(), request.getIdVeiculo(), request.getDescricaoQueixas())
        );
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listarOrdensDeServico() {
        List<OrdemDeServico> response = listarOrdemDeServico.listarOrdemDeServico();
        return ResponseEntity.ok(response);
    }
}
