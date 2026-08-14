package com.oficina.estoque.infrastructure.adapters.inbound.rest;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.domain.ports.inbound.*;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoRequestDTO;
import com.oficina.estoque.infrastructure.adapters.inbound.rest.dto.ServicoResponseDTO;
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
@RequestMapping("api/v1/servicos")
@Tag(name = "Serviços", description = "Gestão de serviços oferecidos pela oficina")
@SecurityRequirement(name = "bearerAuth")
public class ServicoController {

    ListarServicoUseCase listarServicos;
    CadastrarServicoUseCase cadastrarServico;
    AtualizarServicoUseCase atualizarServico;
    RemoverServicoUseCase removerServico;
    BuscarServicoUseCase buscarServico;

    ServicoController(ListarServicoUseCase listarServicos, CadastrarServicoUseCase cadastrarServico,
                      AtualizarServicoUseCase atualizarServico, RemoverServicoUseCase removerServico, BuscarServicoUseCase buscarServico){
        this.listarServicos = listarServicos;
        this.cadastrarServico = cadastrarServico;
        this.atualizarServico = atualizarServico;
        this.removerServico = removerServico;
        this.buscarServico = buscarServico;
    }

    @GetMapping
    @Operation(summary = "Listar serviços", description = "Retorna uma lista de todos os serviços cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de serviços retornada com sucesso")
    public ResponseEntity<List<ServicoResponseDTO>> listar() {
        List<ServicoResponseDTO> response = listarServicos.listarServico().stream()
                .map(ServicoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Cadastrar serviço", description = "Cadastra um novo tipo de serviço no sistema")
    @ApiResponse(responseCode = "201", description = "Serviço cadastrado com sucesso")
    public ResponseEntity<ServicoResponseDTO> criar(@Valid @RequestBody ServicoRequestDTO request) {
        Servico novoServico = cadastrarServico.cadastrarServico(request.descricao(), request.valor());
        ServicoResponseDTO response = new ServicoResponseDTO(novoServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID", description = "Retorna os detalhes de um serviço específico")
    @ApiResponse(responseCode = "200", description = "Serviço encontrado")
    @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@Parameter(description = "ID do serviço") @PathVariable int id) {
        Servico servico = buscarServico.buscarServico(id);
        return ResponseEntity.ok(new ServicoResponseDTO(servico.getId(), servico.getDescricao(), servico.getValor()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover serviço", description = "Remove o cadastro de um serviço do sistema")
    @ApiResponse(responseCode = "200", description = "Serviço removido com sucesso")
    public ResponseEntity<Void> remover(@Parameter(description = "ID do serviço") @PathVariable int id) {
        removerServico.removerServico(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar serviço", description = "Atualiza os dados de um serviço existente")
    @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso")
    public ResponseEntity<ServicoResponseDTO>  atualizar(@Parameter(description = "ID do serviço") @PathVariable int id, @Valid @RequestBody ServicoRequestDTO request) {
        Servico servico = new Servico(id, request.descricao(), request.valor());
        atualizarServico.atualizarServico(servico);
        return ResponseEntity.ok(new ServicoResponseDTO(servico));
    }

}
