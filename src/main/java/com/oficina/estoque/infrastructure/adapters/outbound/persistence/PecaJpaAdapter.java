package com.oficina.estoque.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.mapper.PecaPersistenceMapper;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PecaJpaAdapter implements PecaRepositoryPort {
    private final PecaJpaRepository repository;
    private final PecaPersistenceMapper mapper;

    public PecaJpaAdapter(PecaJpaRepository repository, PecaPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Peca salvar(Peca peca) {
        return mapper.toDomain(repository.save(mapper.toEntity(peca)));
    }

    @Override
    public Optional<Peca> buscarPorId(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    public List<Peca> listarPecas() {
        List<Peca> pecas = repository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
        return pecas;
    }
}
