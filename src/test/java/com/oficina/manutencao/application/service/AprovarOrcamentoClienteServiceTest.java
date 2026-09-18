package com.oficina.manutencao.application.service;

import com.oficina.manutencao.domain.model.OrdemDeServico;
import com.oficina.manutencao.domain.model.StatusOS;
import com.oficina.manutencao.domain.ports.outbound.OrdemDeServicoRepositoryPort;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AprovarOrcamentoClienteServiceTest {
    private final OrdemDeServicoRepositoryPort repository = mock(OrdemDeServicoRepositoryPort.class);
    private final AprovarOrcamentoClienteService service = new AprovarOrcamentoClienteService(repository);

    @Test
    void deveAprovarComDocumentoDoCliente() {
        OrdemDeServico ordem = criarOrdem();
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(repository.salvar(any())).thenReturn(ordem);

        service.aprovarOrcamento(1, "123.456.789-00", true);

        assertEquals(StatusOS.EM_EXECUCAO, ordem.getStatus());
    }

    @Test
    void deveRejeitarComDocumentoDoCliente() {
        OrdemDeServico ordem = criarOrdem();
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));
        when(repository.salvar(any())).thenReturn(ordem);

        service.aprovarOrcamento(1, "12345678900", false);

        assertEquals(StatusOS.FINALIZADA, ordem.getStatus());
    }

    @Test
    void deveRecusarDocumentoDeOutraOrdem() {
        OrdemDeServico ordem = criarOrdem();
        when(repository.buscarPorId(1)).thenReturn(Optional.of(ordem));

        assertThrows(IllegalArgumentException.class,
                () -> service.aprovarOrcamento(1, "99999999999", true));
        verify(repository, never()).salvar(any());
    }

    private OrdemDeServico criarOrdem() {
        LocalDateTime agora = LocalDateTime.now();
        return OrdemDeServico.builder()
                .id(1)
                .documentoCliente("12345678900")
                .placaVeiculo("ABC1234")
                .servicos(List.of())
                .pecasNecessarias(List.of())
                .status(StatusOS.AGUARDANDO_APROVACAO)
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .descricaoQueixas("Problema")
                .build();
    }
}
