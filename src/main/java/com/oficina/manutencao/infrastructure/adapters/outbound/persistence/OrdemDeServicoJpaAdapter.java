package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.OrdemDeServicoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.OrdemDeServicoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrdemDeServicoJpaAdapter implements OrdemDeServicoRepositoryPort {

    private final OrdemDeServicoJpaRepository repository;
    private final OrdemDeServicoPersistenceMapper mapper;
    private final PecaJpaRepository pecaRepository;
    private final ServicoJpaRepository servicoRepository;

    public OrdemDeServicoJpaAdapter(
            OrdemDeServicoJpaRepository repository,
            OrdemDeServicoPersistenceMapper mapper,
            PecaJpaRepository pecaRepository,
            ServicoJpaRepository servicoRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.pecaRepository = pecaRepository;
        this.servicoRepository = servicoRepository;
    }

    @Override
    public OrdemDeServico salvar(OrdemDeServico ordem) {
        Map<Integer, PecaEntity> pecasRef = ordem.getPecasNecessarias().stream()
                .collect(Collectors.toMap(
                        p -> p.peca().getId(),
                        p -> pecaRepository.getReferenceById(p.peca().getId())
                ));

        Map<Integer, ServicoEntity> servicosRef = ordem.getServicos().stream()
                .collect(Collectors.toMap(
                        s -> s.getId(),
                        s -> servicoRepository.getReferenceById(s.getId())
                ));

        OrdemDeServicoEntity entity = mapper.toEntity(ordem, pecasRef, servicosRef);
        OrdemDeServicoEntity salva = repository.save(entity);
        return mapper.toDomain(salva);
    }

    @Override
    public Optional<OrdemDeServico> buscarPorId(int id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<OrdemDeServico> listarTodos() {
        List<OrdemDeServico> ordemDeServicos = repository.findAll().stream().map(mapper::toDomain).toList();
        return ordemDeServicos;
    }
}
