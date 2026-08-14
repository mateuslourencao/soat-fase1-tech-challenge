package com.oficina.manutencao.domain.model;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.model.Servico;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdemDeServicoDomainTest {

    @Test void deveCriarOrdemDeServicoComStatusRecebida() {
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Barulho no motor");
        
        assertEquals(StatusOS.RECEBIDA, os.getStatus());
        assertEquals("123", os.getDocumentoCliente());
        assertEquals("ABC1234", os.getPlacaVeiculo());
        assertTrue(os.getServicos().isEmpty());
        assertTrue(os.getPecasNecessarias().isEmpty());
        assertEquals(0.0, os.getOrcamento());
        assertNotNull(os.getDataCriacao());
    }

    @Test void deveRegistrarAtualizacaoDeItensECalcularOrcamento() {
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Revisao");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Peca peca = new Peca(1, "Oleo", 50.0, 10);
        PecasNecessarias itemPeca = new PecasNecessarias(peca, 2); // 100.0
        Servico servico = new Servico(1, "Mao de obra", 150.0); // 150.0
        
        os.registrarAtualizacaoDeItens(List.of(itemPeca), List.of(servico));
        
        assertEquals(250.0, os.getOrcamento());
        assertEquals(1, os.getPecasNecessarias().size());
        assertEquals(1, os.getServicos().size());
        assertNotEquals(os.getDataCriacao(), os.getDataAtualizacao());
    }

    @Test void deveAlterarStatus() {
        OrdemDeServico os = new OrdemDeServico("123", "ABC1234", "Revisao");
        os.alterarStatus(StatusOS.EM_DIAGNOSTICO);
        
        assertEquals(StatusOS.EM_DIAGNOSTICO, os.getStatus());
    }

    @Test void deveLancarExcecaoAoCriarPecaNecessariaInvalida() {
        assertThrows(IllegalArgumentException.class, () -> new PecasNecessarias(null, 5));
        Peca peca = new Peca(1, "Oleo", 50.0, 10);
        assertThrows(IllegalArgumentException.class, () -> new PecasNecessarias(peca, 0));
        assertThrows(IllegalArgumentException.class, () -> new PecasNecessarias(peca, -1));
    }

    @Test void deveCalcularValorTotalDaPecaNecessaria() {
        Peca peca = new Peca(1, "Oleo", 50.0, 10);
        PecasNecessarias item = new PecasNecessarias(peca, 3);
        
        assertEquals(150.0, item.getValorTotal());
        assertEquals(50.0, item.getValorUnitario());
    }
}
