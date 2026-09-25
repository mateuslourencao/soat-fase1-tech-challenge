package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.repository;

import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrdemDeServicoJpaRepository extends JpaRepository<OrdemDeServicoEntity, Integer> {
    @Query("select os.status from OrdemDeServicoEntity os where os.id = :id")
    Optional<StatusOS> buscarStatusPorId(@Param("id") int id);
}
