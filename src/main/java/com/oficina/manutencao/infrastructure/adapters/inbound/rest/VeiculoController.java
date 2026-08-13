package com.oficina.manutencao.infrastructure.adapters.inbound.rest;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.inbound.*;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoRequestDTO;
import com.oficina.manutencao.infrastructure.adapters.inbound.rest.dto.VeiculoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        Veiculo veiculo = new Veiculo(request.placa(), request.marca(), request.modelo(), request.ano());
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

    @GetMapping("/{placa}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(@PathVariable String placa) {
        Veiculo veiculo = buscarVeiculo.buscarVeiculo(placa);
        return ResponseEntity.ok(converterParaDTO(veiculo));
    }

    @PutMapping("/{placa}")
    public ResponseEntity<VeiculoResponseDTO> atualizar(@PathVariable String placa, @RequestBody @Valid VeiculoRequestDTO request) {
        Veiculo veiculoAtualizado = new Veiculo(placa, request.marca(), request.modelo(), request.ano());
        Veiculo salvo = atualizarVeiculo.atualizarVeiculo(placa, veiculoAtualizado);
        return ResponseEntity.ok(converterParaDTO(salvo));
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> remover(@PathVariable String placa) {
        removerVeiculo.removerVeiculo(placa);
        return ResponseEntity.noContent().build();
    }

    private VeiculoResponseDTO converterParaDTO(Veiculo veiculo) {
        return new VeiculoResponseDTO(veiculo.getPlaca(), veiculo.getMarca(), veiculo.getModelo(), veiculo.getAno());
    }
}