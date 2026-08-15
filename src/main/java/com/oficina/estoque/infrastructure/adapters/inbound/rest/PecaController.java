package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ObterPecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaResponseDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ReporPecaRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pecas")
@Tag(name = "Peças", description = "Gestão de peças em estoque")
@SecurityRequirement(name = "bearerAuth")
public class PecaController {

    private final CadastrarPecaUseCase cadastrarPeca;
    private final ObterPecaUseCase obterPeca;
    private final ListarPecaUseCase listarPeca;
    private final ReporPecaUseCase reporPeca;
    private final AtualizarPecaUseCase atualizarPeca;
    private final RemoverPecaUseCase removerPeca;

    PecaController(CadastrarPecaUseCase cadastrarPeca,
                   ObterPecaUseCase obterPeca,
                   ListarPecaUseCase listarPeca,
                   ReporPecaUseCase reporPeca,
                   AtualizarPecaUseCase atualizarPeca,
                   RemoverPecaUseCase removerPeca) {
        this.cadastrarPeca = cadastrarPeca;
        this.obterPeca = obterPeca;
        this.listarPeca = listarPeca;
        this.reporPeca = reporPeca;
        this.atualizarPeca = atualizarPeca;
        this.removerPeca = removerPeca;
    }

    @GetMapping
    @Operation(summary = "Listar peças", description = "Retorna uma lista de todas as peças cadastradas no estoque")
    @ApiResponse(responseCode = "200", description = "Lista de peças retornada com sucesso")
    public ResponseEntity<List<PecaResponseDTO>> listarPecas() {
        List<PecaResponseDTO> response = listarPeca.listarPecas().stream()
                .map(PecaResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/criar")
    @Operation(summary = "Cadastrar peça", description = "Cadastra uma nova peça no estoque")
    @ApiResponse(responseCode = "201", description = "Peça cadastrada com sucesso")
    public ResponseEntity<PecaResponseDTO> cadastrarPeca(@Valid @RequestBody PecaRequestDTO request) {
        Peca novaPeca = cadastrarPeca.cadastrarPeca(request.descricao(), request.valor(), request.quantidade());
        PecaResponseDTO response = new PecaResponseDTO(novaPeca);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/obter")
    @Operation(summary = "Dar baixa em peça", description = "Retira uma quantidade de peças do estoque")
    @ApiResponse(responseCode = "200", description = "Baixa realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Quantidade insuficiente em estoque")
    public ResponseEntity<Object> obterPeca(@Valid @RequestBody ObterPecaRequestDTO request) {
        obterPeca.obterPeca(request.id(), request.quantidade());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/repor")
    @Operation(summary = "Repor estoque de peça", description = "Adiciona uma quantidade de peças ao estoque existente")
    @ApiResponse(responseCode = "200", description = "Reposição realizada com sucesso")
    public ResponseEntity<Object> reporPeca(@Valid @RequestBody ReporPecaRequestDTO request) {
        reporPeca.reporEstoque(request.id(), request.quantidade());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar peça", description = "Atualiza os dados de uma peça existente")
    @ApiResponse(responseCode = "200", description = "Peça atualizada com sucesso")
    public ResponseEntity<PecaResponseDTO> atualizarPeca(@PathVariable int id, @Valid @RequestBody PecaRequestDTO request) {
        Peca pecaAtualizada = atualizarPeca.atualizarPeca(new Peca(id, request.descricao(), request.valor(), request.quantidade()));
        return ResponseEntity.ok(new PecaResponseDTO(pecaAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover peça", description = "Remove uma peça do estoque")
    @ApiResponse(responseCode = "204", description = "Peça removida com sucesso")
    public ResponseEntity<Void> removerPeca(@PathVariable int id) {
        removerPeca.removerPeca(id);
        return ResponseEntity.noContent().build();
    }
}
