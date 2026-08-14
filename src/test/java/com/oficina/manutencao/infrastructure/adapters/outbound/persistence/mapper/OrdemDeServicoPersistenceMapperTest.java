package com.oficina.manutencao.infrastructure.adapters.outbound.persistence.mapper;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.PecaEntity;
import com.oficina.estoque.infrastructure.adapters.outbound.persistence.entity.ServicoEntity;
import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.PecasNecessarias;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.OrdemDeServicoServicosEntity;
import com.oficina.manutencao.infrastructure.adapters.outbound.persistence.entity.PecaNecessariaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrdemDeServicoPersistenceMapperTest {
    private final OrdemDeServicoPersistenceMapper mapper = new OrdemDeServicoPersistenceMapper();

    @Test void deveConverterParaEntity() {
        Peca peca = new Peca(1, "Filtro", 30.0, 10);
        Servico servico = new Servico(2, "Mao de obra", 100.0);
        
        OrdemDeServico os = new OrdemDeServico(1, "123", "ABC1234", 
                List.of(servico), 
                List.of(new PecasNecessarias(peca, 2)), 
                160.0, StatusOS.RECEBIDA, LocalDateTime.now(), LocalDateTime.now(), "Queixa", "Diag");

        PecaEntity pecaEntity = new PecaEntity(1, "Filtro", BigDecimal.valueOf(30.0), 10);
        ServicoEntity servicoEntity = new ServicoEntity(2, "Mao de obra", BigDecimal.valueOf(100.0));

        OrdemDeServicoEntity entity = mapper.toEntity(os, 
                Map.of(1, pecaEntity), 
                Map.of(2, servicoEntity));

        assertEquals(1, entity.getId());
        assertEquals("123", entity.getDocumentoCliente());
        assertEquals(BigDecimal.valueOf(160.0), entity.getOrcamento());
        assertEquals(1, entity.getPecasNecessarias().size());
        assertEquals(1, entity.getServicos().size());
        assertEquals(1, entity.getPecasNecessarias().get(0).getPeca().getId());
        assertEquals(2, entity.getServicos().get(0).getServico().getId());
    }

    @Test void deveConverterParaDomain() {
        PecaEntity pecaEntity = new PecaEntity(1, "Filtro", BigDecimal.valueOf(30.0), 10);
        ServicoEntity servicoEntity = new ServicoEntity(2, "Mao de obra", BigDecimal.valueOf(100.0));
        
        OrdemDeServicoEntity entity = new OrdemDeServicoEntity();
        entity.setId(1);
        entity.setDocumentoCliente("123");
        entity.setPlacaVeiculo("ABC1234");
        entity.setOrcamento(BigDecimal.valueOf(160.0));
        entity.setStatus(StatusOS.EM_DIAGNOSTICO);
        entity.setDataCriacao(LocalDateTime.now());
        entity.setDescricaoQueixas("Queixa");
        
        PecaNecessariaEntity pecaItem = new PecaNecessariaEntity(1, pecaEntity, 2, BigDecimal.valueOf(30.0));
        entity.setPecasNecessarias(List.of(pecaItem));
        
        OrdemDeServicoServicosEntity servicoItem = new OrdemDeServicoServicosEntity(1, servicoEntity, BigDecimal.valueOf(100.0));
        entity.setServicos(List.of(servicoItem));

        OrdemDeServico os = mapper.toDomain(entity);

        assertEquals(1, os.getId());
        assertEquals(160.0, os.getOrcamento());
        assertEquals(1, os.getPecasNecessarias().size());
        assertEquals(1, os.getServicos().size());
        assertEquals(30.0, os.getPecasNecessarias().get(0).getValorUnitario());
        assertEquals(100.0, os.getServicos().get(0).getValor());
    }
}
