package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.CadastrarPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ListarPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ObterPecaUseCase;
import com.oficina.estoque.domain.ports.inbound.ReporPecaUseCase;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ObterPecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaResponseDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ReporPecaRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pecas")
public class PecaController {

    private final CadastrarPecaUseCase cadastrarPeca;
    private final ObterPecaUseCase obterPeca;
    private final ListarPecaUseCase listarPeca;
    private final ReporPecaUseCase reporPeca;

    PecaController(CadastrarPecaUseCase cadastrarPeca, ObterPecaUseCase obterPeca, ListarPecaUseCase listarPeca, ReporPecaUseCase reporPeca) {
        this.cadastrarPeca = cadastrarPeca;
        this.obterPeca = obterPeca;
        this.listarPeca = listarPeca;
        this.reporPeca = reporPeca;
    }

    @GetMapping
    public ResponseEntity<List<Peca>> listarPecas() {
        return ResponseEntity.ok(listarPeca.listarPecas());
    }

    @PostMapping("/criar")
    public ResponseEntity<PecaResponseDTO> cadastrarPeca(@Valid @RequestBody PecaRequestDTO request) {
        Peca novaPeca = cadastrarPeca.cadastrarPeca(request.descricao(), request.valor(), request.quantidade());
        PecaResponseDTO response = new PecaResponseDTO(novaPeca);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/obter")
    public ResponseEntity<Object> ObterPeca(@Valid @RequestBody ObterPecaRequestDTO request) {
        obterPeca.obterPeca(request.id(), request.quantidade());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/repor")
    public ResponseEntity<Object> ReporPeca(@Valid @RequestBody ReporPecaRequestDTO request) {
        reporPeca.reporEstoque(request.id(), request.quantidade());
        return ResponseEntity.ok().build();
    }

}
