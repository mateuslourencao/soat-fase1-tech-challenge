package com.oficina.manutencao.infrastructure.adapters.outbound.persistence;

import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.PecaJpaRepository;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository.ServicoJpaRepository;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper.OrdemDeServicoPersistenceMapper;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository.OrdemDeServicoJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrdemDeServicoJpaAdapter implements OrdemDeServicoRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(OrdemDeServicoJpaAdapter.class);

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
                        Servico::getId,
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
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    public List<OrdemDeServico> listarOrdenadoEmAberto() {
        return repository.findAll().stream()
                .filter(this::possuiDadosObrigatoriosParaListagem)
                .filter(entity -> entity.getStatus() != StatusOS.FINALIZADA)
                .filter(entity -> entity.getStatus() != StatusOS.ENTREGUE)
                .map(mapper::toDomain)
                .sorted(Comparator.comparing(OrdemDeServico::getDataCriacao))
                .toList();
    }

    private boolean possuiDadosObrigatoriosParaListagem(OrdemDeServicoEntity entity) {
        if (entity.getStatus() == null || entity.getDataCriacao() == null) {
            logger.warn("Ignorando ordem de serviço inconsistente na listagem de abertas: status ou data de criação ausente");
            return false;
        }
        return true;
    }
    @Override
    public List<OrdemDeServico> buscarOrdensdeServicoPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findAll().stream()
                .filter(os->os.getStatus() == StatusOS.FINALIZADA)
                .filter(os->os.getDataAtualizacao().isAfter(inicio))
                .filter(os->os.getDataAtualizacao().isBefore(fim))
                .map(mapper::toDomain).toList();
    }
}
