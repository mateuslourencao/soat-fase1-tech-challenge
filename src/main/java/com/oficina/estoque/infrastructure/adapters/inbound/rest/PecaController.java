package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.inbound.CadastrarPecaUseCase;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.PecaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("api/v1/pecas")
public class PecaController {

    private final CadastrarPecaUseCase cadastrarPeca;
    PecaController(CadastrarPecaUseCase cadastrarPeca) {
        this.cadastrarPeca = cadastrarPeca;
    }

    @PostMapping
    public ResponseEntity<PecaResponseDTO> cadastrarPeca(@Valid @RequestBody PecaRequestDTO request) {
        Peca novaPeca = cadastrarPeca.CadastrarPeca(request.descricao(), request.valor(), request.quantidade());
        PecaResponseDTO response = new PecaResponseDTO(novaPeca);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
