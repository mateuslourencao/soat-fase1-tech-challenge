package com.oficina.administrativo.infrastructure.adapters.inbound.rest;

import com.oficina.administrativo.domain.model.FuncionarioAutenticado;
import com.oficina.administrativo.domain.ports.inbound.AutenticarFuncionarioUseCase;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.AutenticacaoRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioAutenticadoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Autenticação", description = "Endpoints para autenticação de funcionários")
public class AutenticacaoFuncionarioController {
    private final AutenticarFuncionarioUseCase autenticarFuncionario;

    public AutenticacaoFuncionarioController(AutenticarFuncionarioUseCase autenticarFuncionario) {
        this.autenticarFuncionario = autenticarFuncionario;
    }

    @PostMapping
    @Operation(summary = "Autenticar funcionário", description = "Realiza o login do funcionário e retorna um token JWT")
    @ApiResponse(responseCode = "200", description = "Autenticado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
    public ResponseEntity<FuncionarioAutenticadoResponseDTO> autenticar(@Valid @RequestBody AutenticacaoRequestDTO request) {
        try {
            FuncionarioAutenticado autenticacao = autenticarFuncionario.autenticar(request.email(), request.senha());
            return ResponseEntity.ok(new FuncionarioAutenticadoResponseDTO(autenticacao));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
    }
}
