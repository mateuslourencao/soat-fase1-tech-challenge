package com.oficina.estoque.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PecaJpaRepository extends JpaRepository<PecaEntity, UUID> {}
