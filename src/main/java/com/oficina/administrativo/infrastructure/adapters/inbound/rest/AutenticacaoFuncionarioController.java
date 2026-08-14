package com.oficina.administrativo.infrastructure.adapters.inbound.rest;

import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.ports.inbound.AutenticarFuncionarioUseCase;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.AutenticacaoRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioAutenticadoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/administrativo/autenticacao")
public class AutenticacaoFuncionarioController {
    private final AutenticarFuncionarioUseCase autenticarFuncionario;

    public AutenticacaoFuncionarioController(AutenticarFuncionarioUseCase autenticarFuncionario) {
        this.autenticarFuncionario = autenticarFuncionario;
    }

    @PostMapping
    public ResponseEntity<FuncionarioAutenticadoResponseDTO> autenticar(@Valid @RequestBody AutenticacaoRequestDTO request) {
        try {
            FuncionarioAutenticado autenticacao = autenticarFuncionario.autenticar(request.email(), request.senha());
            return ResponseEntity.ok(new FuncionarioAutenticadoResponseDTO(autenticacao));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }
}
