package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrdemDeServicoJpaRepository extends JpaRepository<OrdemDeServicoEntity, UUID> {}
