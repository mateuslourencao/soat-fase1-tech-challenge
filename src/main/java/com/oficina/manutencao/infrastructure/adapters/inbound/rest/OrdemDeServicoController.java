package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.application.service.AprovarOrcamentoService;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.inbound.CriarOrdemDeServicoUseCase;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ordemdeservico")
class OrdemDeServicoController {

    private final CriarOrdemDeServicoUseCase criarOrdemDeServico;
    OrdemDeServicoController(CriarOrdemDeServicoUseCase criarOrdemDeServico) {
        this.criarOrdemDeServico = criarOrdemDeServico;
    }

    @PostMapping
    public ResponseEntity<Void> criarOrdemDeServico(@Valid @RequestBody @NonNull OrdemDeServico request) {
        criarOrdemDeServico.criarOrdemDeServico(
                new OrdemDeServico(UUID.randomUUID(), request.getIdCliente(), request.getIdVeiculo(), request.getDescricaoQueixas())
        );
        return ResponseEntity.ok().build();
    }

    //para teste
    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listarOrdemDeServicos() {
        var response = new ArrayList<OrdemDeServico>();
        return ResponseEntity.ok().body(response);
    }
}
