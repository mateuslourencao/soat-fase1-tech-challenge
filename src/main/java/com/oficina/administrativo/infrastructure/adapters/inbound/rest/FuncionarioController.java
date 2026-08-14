package com.oficina.administrativo.infrastructure.adapters.inbound.rest;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.inbound.*;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.AtualizarFuncionarioRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioRequestDTO;
import com.oficina.administrativo.infrastructure.adapters.inbound.rest.dto.FuncionarioResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/administrativo/funcionarios")
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
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@RequestBody @Valid FuncionarioRequestDTO request) {
        Funcionario funcionario = new Funcionario(0, request.nome(), request.email(), null, request.perfil(), true);
        Funcionario criado = cadastrarFuncionario.cadastrarFuncionario(funcionario, request.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(criado));
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listar() {
        List<FuncionarioResponseDTO> funcionarios = listarFuncionarios.listarFuncionarios().stream()
                .map(this::converterParaDTO)
                .toList();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarPorId(@PathVariable int id) {
        return ResponseEntity.ok(converterParaDTO(buscarFuncionario.buscarFuncionario(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizar(@PathVariable int id,
                                                              @RequestBody @Valid AtualizarFuncionarioRequestDTO request) {
        Funcionario funcionario = new Funcionario(id, request.nome(), request.email(), null, request.perfil(), true);
        return ResponseEntity.ok(converterParaDTO(atualizarFuncionario.atualizarFuncionario(id, funcionario)));
    }

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativar(@PathVariable int id) {
        ativarFuncionario.ativarFuncionario(id);
    }

    @PatchMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativar(@PathVariable int id) {
        inativarFuncionario.inativarFuncionario(id);
    }

    private FuncionarioResponseDTO converterParaDTO(Funcionario funcionario) {
        return new FuncionarioResponseDTO(funcionario);
    }
}
