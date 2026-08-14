package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veiculos")
@Tag(name = "Veículos", description = "Gestão de veículos dos clientes")
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculo;
    private final AtualizarVeiculoUseCase atualizarVeiculo;
    private final BuscarVeiculoUseCase  buscarVeiculo;
    private final ListarVeiculosUseCase listarVeiculos;
    private final RemoverVeiculoUseCase removerVeiculo;

    public VeiculoController(CadastrarVeiculoUseCase cadastrarVeiculo, AtualizarVeiculoUseCase atualizarVeiculo,
                             BuscarVeiculoUseCase buscarVeiculo, ListarVeiculosUseCase listarVeiculos,
                             RemoverVeiculoUseCase removerVeiculo) {
        this.cadastrarVeiculo = cadastrarVeiculo;
        this.atualizarVeiculo = atualizarVeiculo;
        this.buscarVeiculo = buscarVeiculo;
        this.listarVeiculos = listarVeiculos;
        this.removerVeiculo = removerVeiculo;
    }

    @PostMapping
    @Operation(summary = "Cadastrar veículo", description = "Cadastra um novo veículo no sistema")
    @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso")
    public ResponseEntity<VeiculoResponseDTO> criar(@RequestBody @Valid VeiculoRequestDTO request) {
        Veiculo veiculo = new Veiculo(request.placa(), request.marca(), request.modelo(), request.ano());
        Veiculo salvo = cadastrarVeiculo.cadastrarVeiculo(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salvo));
    }

    @GetMapping
    @Operation(summary = "Listar veículos", description = "Retorna uma lista de todos os veículos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")
    public ResponseEntity<List<VeiculoResponseDTO>> listar() {
        List<VeiculoResponseDTO> response = listarVeiculos.listarVeiculos().stream()
                .map(this::converterParaDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{placa}")
    @Operation(summary = "Buscar veículo por placa", description = "Retorna os detalhes de um veículo através da sua placa")
    @ApiResponse(responseCode = "200", description = "Veículo encontrado")
    @ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(@Parameter(description = "Placa do veículo") @PathVariable String placa) {
        Veiculo veiculo = buscarVeiculo.buscarVeiculo(placa);
        return ResponseEntity.ok(converterParaDTO(veiculo));
    }

    @PutMapping("/{placa}")
    @Operation(summary = "Atualizar veículo", description = "Atualiza os dados de um veículo existente")
    @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso")
    public ResponseEntity<VeiculoResponseDTO> atualizar(@Parameter(description = "Placa do veículo") @PathVariable String placa, @RequestBody @Valid VeiculoRequestDTO request) {
        Veiculo veiculoAtualizado = new Veiculo(placa, request.marca(), request.modelo(), request.ano());
        Veiculo salvo = atualizarVeiculo.atualizarVeiculo(placa, veiculoAtualizado);
        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    @DeleteMapping("/{placa}")
    @Operation(summary = "Remover veículo", description = "Remove o cadastro de um veículo do sistema")
    @ApiResponse(responseCode = "204", description = "Veículo removido com sucesso")
    public ResponseEntity<Void> remover(@Parameter(description = "Placa do veículo") @PathVariable String placa) {
        removerVeiculo.removerVeiculo(placa);
        return ResponseEntity.noContent().build();
    }

    private VeiculoResponseDTO converterParaDTO(Veiculo veiculo) {
        return new VeiculoResponseDTO(veiculo.getPlaca(), veiculo.getMarca(), veiculo.getModelo(), veiculo.getAno());
    }
}