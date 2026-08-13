package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.application.service.*;
import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/veiculos")
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
    public ResponseEntity<VeiculoResponseDTO> criar(@RequestBody @Valid VeiculoRequestDTO request) {
        Veiculo veiculo = new Veiculo(UUID.randomUUID(), request.placa(), request.marca(), request.modelo(), request.ano());
        Veiculo salvo = cadastrarVeiculo.cadastrarVeiculo(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salvo));
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> listar() {
        List<VeiculoResponseDTO> veiculos = listarVeiculos.listarVeiculos().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(@PathVariable UUID id) {
        Veiculo veiculo = buscarVeiculo.buscarVeiculo(id);
        return ResponseEntity.ok(converterParaDTO(veiculo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid VeiculoRequestDTO request) {
        Veiculo veiculoAtualizado = new Veiculo(null, request.placa(), request.marca(), request.modelo(), request.ano());
        Veiculo salvo = atualizarVeiculo.atualizarVeiculo(id, veiculoAtualizado);
        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        removerVeiculo.removerVeiculo(id);
        return ResponseEntity.noContent().build();
    }

    private VeiculoResponseDTO converterParaDTO(Veiculo veiculo) {
        return new VeiculoResponseDTO(veiculo.getId(), veiculo.getPlaca(), veiculo.getMarca(), veiculo.getModelo(), veiculo.getAno());
    }
}