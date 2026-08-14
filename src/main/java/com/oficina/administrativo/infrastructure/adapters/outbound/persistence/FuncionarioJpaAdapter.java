package com.oficina.administrativo.infrastructure.adapters.outbound.persistence;

import com.oficina.administrativo.domain.model.Funcionario;
import com.oficina.administrativo.domain.ports.outbound.FuncionarioRepositoryPort;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.entity.FuncionarioEntity;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.mapper.FuncionarioPersistenceMapper;
import com.oficina.administrativo.infrastructure.adapters.outbound.persistence.repository.FuncionarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

@Component
public class FuncionarioJpaAdapter implements FuncionarioRepositoryPort {
    private final FuncionarioJpaRepository repository;
    private final FuncionarioPersistenceMapper mapper;

    public FuncionarioJpaAdapter(FuncionarioJpaRepository repository, FuncionarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        return repository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<Funcionario> buscarPorId(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Funcionario> listarTodos() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        FuncionarioEntity entity = mapper.toEntity(funcionario);
        FuncionarioEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
