package com.oficina.estoque.application.service;

import com.oficina.estoque.domain.model.Peca;
import com.oficina.estoque.domain.ports.outbound.PecaRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class PecaServiceTest {

    @Test
    void deveReporEstoqueDaPeca() {
        Peca pecaTemp = new Peca(1, "Filtro de oleo", 35.0, 10);
        PecaRepositoryEmMemoria repositoryEmMemoria = new PecaRepositoryEmMemoria(pecaTemp);
        PecaService service = new PecaService(repositoryEmMemoria);

        Peca pecaReposta = service.reporEstoque(1, 5);

        assertEquals(15, pecaReposta.getQuantidade());
        assertSame(pecaReposta, repositoryEmMemoria.pecaSalva);
    }

    @Test
    void deveListarPecas() {
        Peca pecaTemp = new Peca(1, "Filtro de oleo", 35.0, 10);
        PecaRepositoryEmMemoria repositoryMemoria = new PecaRepositoryEmMemoria(pecaTemp);
        PecaService service = new PecaService(repositoryMemoria);
        List<Peca> listaPecasEsperada = Arrays.asList(pecaTemp);

        List<Peca> pecas = service.listarPecas();

        assertEquals(pecas, listaPecasEsperada);

    }

    @Test
    void deveObterPeca() {
        Peca pecaTemp = new Peca(1, "Filtro de oleo", 35.0, 10);
        PecaRepositoryEmMemoria repositoryMemoria = new PecaRepositoryEmMemoria(pecaTemp);
        PecaService service = new PecaService(repositoryMemoria);

        Peca peca = service.obterPeca(1,1);

        assertEquals(peca.getQuantidade(), 9);
    }

    @Test
    void deveCadastrarPeca() {
        Peca pecaTemp = new Peca("Filtro de oleo", 35.0, 10);
        PecaRepositoryEmMemoria repositoryMemoria = new PecaRepositoryEmMemoria();
        PecaService service = new PecaService(repositoryMemoria);

        Peca peca = service.cadastrarPeca(pecaTemp.getDescricao(), pecaTemp.getValor(), 10);

        assertEquals(peca.getDescricao(), "Filtro de oleo");
        assertEquals(pecaTemp.getValor(), peca.getValor());
        assertEquals(pecaTemp.getQuantidade(), 10);

    }

    private static class PecaRepositoryEmMemoria implements PecaRepositoryPort {
        private Peca pecaTemporaria;
        private Peca pecaSalva;

        private PecaRepositoryEmMemoria(Peca pecaTemporaria) {
            this.pecaTemporaria = pecaTemporaria;
        }

        private PecaRepositoryEmMemoria() {}

        @Override
        public Peca salvar(Peca peca) {
            pecaSalva = peca;
            return peca;
        }

        @Override
        public Optional<Peca> buscarPorId(int id) {
            return pecaTemporaria.getId() == id ? Optional.of(pecaTemporaria) : Optional.empty();
        }

        @Override
        public List<Peca> listarPecas() {
            return List.of(pecaTemporaria);
        }
    }
}
