package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.manutencao.domain.model.Veiculo;
import com.oficina.manutencao.domain.ports.outbound.VeiculoRepositoryPort;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.VeiculoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.VeiculoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VeiculoJpaAdapter implements VeiculoRepositoryPort {
    private final VeiculoJpaRepository repository;
    private final VeiculoPersistenceMapper mapper;

    public VeiculoJpaAdapter(VeiculoJpaRepository repository, VeiculoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        return mapper.toDomain(repository.save(mapper.toEntity(veiculo)));
    }

    @Override
    public Optional<Veiculo> buscarPorId(String placa) {
        return repository.findById(placa).map(mapper::toDomain);
    }

    @Override
    public List<Veiculo> listarTodos() { return repository.findAll().stream().map(mapper::toDomain).toList(); }

    @Override
    public void remover(String placa) {repository.deleteById(placa);}
}
