package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {}
