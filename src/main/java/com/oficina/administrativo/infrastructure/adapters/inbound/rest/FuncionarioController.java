package com.oficina.administrativo.infrastructure.adapters.inbound.rest;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.*;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.AtualizarFuncionarioRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administrativo/funcionarios")
@Tag(name = "Funcionários", description = "Gestão de funcionários da oficina")
@SecurityRequirement(name = "bearerAuth")
public class FuncionarioController {

    private final CadastrarFuncionarioUseCase cadastrarFuncionario;
    private final BuscarFuncionarioUseCase buscarFuncionario;
    private final ListarFuncionariosUseCase listarFuncionarios;
    private final AtualizarFuncionarioUseCase atualizarFuncionario;
    private final AtivarFuncionarioUseCase ativarFuncionario;
    private final InativarFuncionarioUseCase inativarFuncionario;

    public FuncionarioController(CadastrarFuncionarioUseCase cadastrarFuncionario, BuscarFuncionarioUseCase buscarFuncionario,
                                 ListarFuncionariosUseCase listarFuncionarios, AtualizarFuncionarioUseCase atualizarFuncionario,
                                 AtivarFuncionarioUseCase ativarFuncionario, InativarFuncionarioUseCase inativarFuncionario) {
        this.cadastrarFuncionario = cadastrarFuncionario;
        this.buscarFuncionario = buscarFuncionario;
        this.listarFuncionarios = listarFuncionarios;
        this.atualizarFuncionario = atualizarFuncionario;
        this.ativarFuncionario = ativarFuncionario;
        this.inativarFuncionario = inativarFuncionario;
    }

    @PostMapping
    @Operation(summary = "Cadastrar funcionário", description = "Cadastra um novo funcionário no sistema")
    @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso")
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@RequestBody @Valid FuncionarioRequestDTO request) {
        Funcionario funcionario = new Funcionario(0, request.nome(), request.email(), null, request.perfil(), true);
        Funcionario criado = cadastrarFuncionario.cadastrarFuncionario(funcionario, request.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(criado));
    }

    @GetMapping
    @Operation(summary = "Listar funcionários", description = "Retorna uma lista de todos os funcionários cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    public ResponseEntity<List<FuncionarioResponseDTO>> listar() {
        List<FuncionarioResponseDTO> funcionarios = listarFuncionarios.listarFuncionarios().stream()
                .map(this::converterParaDTO)
                .toList();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionário por ID", description = "Retorna os detalhes de um funcionário específico")
    @ApiResponse(responseCode = "200", description = "Funcionário encontrado")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@Parameter(description = "ID do funcionário") @PathVariable int id) {
        return ResponseEntity.ok(converterParaDTO(buscarFuncionario.buscarFuncionario(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionário", description = "Atualiza os dados de um funcionário existente")
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@Parameter(description = "ID do funcionário") @PathVariable int id,
                                                              @RequestBody @Valid AtualizarFuncionarioRequestDTO request) {
        Funcionario funcionario = new Funcionario(id, request.nome(), request.email(), null, request.perfil(), true);
        return ResponseEntity.ok(converterParaDTO(atualizarFuncionario.atualizarFuncionario(id, funcionario)));
    }

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ativar funcionário", description = "Ativa o cadastro de um funcionário inativo")
    @ApiResponse(responseCode = "204", description = "Funcionário ativado com sucesso")
    public void ativar(@Parameter(description = "ID do funcionário") @PathVariable int id) {
        ativarFuncionario.ativarFuncionario(id);
    }

    @PatchMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Inativar funcionário", description = "Inativa o cadastro de um funcionário")
    @ApiResponse(responseCode = "204", description = "Funcionário inativado com sucesso")
    public void inativar(@Parameter(description = "ID do funcionário") @PathVariable int id) {
        inativarFuncionario.inativarFuncionario(id);
    }

    private FuncionarioResponseDTO converterParaDTO(Funcionario funcionario) {
        return new FuncionarioResponseDTO(funcionario);
    }
}
