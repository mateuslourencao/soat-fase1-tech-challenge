package com.oficina.manutencao.domain.ports.outbound;

import com.oficina.manutencao.domain.model.Cliente;
import com.oficina.manutencao.domain.model.OrdemDeServico;

public interface NotificarClientePort {
    void notificarAtualizacaoStatus(Cliente cliente, OrdemDeServico os);

    default void notificarOrcamentoAguardandoAprovacao(Cliente cliente, OrdemDeServico os) {
        notificarAtualizacaoStatus(cliente, os);
    }
}
